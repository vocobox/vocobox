package org.vocobox.voice.pitch.tarsos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vocobox.events.SoundEvent;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetection;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.GainProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.onsets.PercussionOnsetDetector;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

/**
 * Read pitch changed events from a file as fast as it can. Can't be used for
 * synthetizer control
 */
public class VoiceFileRead extends VoiceTarsos {
    public AudioDispatcher dispatcher;
    public AudioDispatcher sourceDispatcher;
    public GainProcessor estimationGain;
    public GainProcessor sourceGain;
    public PitchEstimationAlgorithm algo = PitchEstimationAlgorithm.YIN;
    public File currentFile;

    public List<SoundEvent> pitchEvents = new ArrayList<SoundEvent>(1000);
    public List<SoundEvent> ampliEvents = new ArrayList<SoundEvent>(1000);
    public List<SoundEvent> onsetEvents = new ArrayList<SoundEvent>();

    public VoiceFileRead() {
    }

    /** return list of sound event, might not be ordered in time 
     * (mainly : ordered amplitude list appears AFTER ordered pitch list : not
     * interlaced)*/
    public List<SoundEvent> read(File file) throws Exception {
        pitchEvents.clear();
        ampliEvents.clear();
        
        currentFile = file;
        AudioFormat format;
        format = AudioSystem.getAudioFileFormat(file).getFormat();
        
        if (format.getChannels() > 1) {
            System.err.println(VoiceFileRead.class.getName() + " STEREO file not supported!");
            // throw new
            // RuntimeException("Seems to be a stereo file! Interrupt because tarsos is very bad with stereo");
        }
        
        float samplerate = format.getSampleRate();
        int size = settings.bufferSize;
        int overlap = settings.overlap;

        configure(file, format, samplerate, size, overlap);
        compute();

        List<SoundEvent> all = new ArrayList<SoundEvent>(pitchEvents.size()+ampliEvents.size());
        all.addAll(pitchEvents);
        all.addAll(ampliEvents);
        return all;
    }

    
    /**
     * 
     * @param fftSize The size of the fft to take (e.g. 512)
     * @param peakThreshold A threshold used for peak picking. Values between 0.1 and 0.8. Default is 0.3, if too many onsets are detected adjust to 0.4 or 0.5.
     * @param silenceThreshold The threshold that defines when a buffer is silent. Default is -70dBSPL. -90 is also used.
     * @param minimumInterOnsetInterval The minimum inter-onset-interval in seconds. When two onsets are detected within this interval the last one does not count. Default is 0.004 seconds.
        double pickThreshold = 0.15;
        double minInterOnsetInterv= 0.25;
        double silenceThreshold = -55;
        double estimationGainValue = 1;

     *
     */
    public void configure(File file, AudioFormat format, float samplerate, int fftSize, int overlap) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        double pickThreshold = 0.15;
        double minInterOnsetInterv= 0.25;
        double silenceThreshold = -55;
        double estimationGainValue = 1;

        dispatcher = AudioDispatcherFactory.fromFile(file, fftSize, overlap);

        addPitchDetection(samplerate, fftSize);
        addGainProcessor(estimationGainValue);
        //addOnsetDetection(fftSize, pickThreshold, minInterOnsetInterv, silenceThreshold);
    }

    public void addPitchDetection(float samplerate, int size) {
        VoiceDetection pitchDetectionHandler = makePitchDetectionHandler(samplerate);
        dispatcher.addAudioProcessor(new PitchProcessor(algo, samplerate, size, pitchDetectionHandler));
    }

    public void addGainProcessor(double estimationGainValue) {
        estimationGain = new GainProcessor(estimationGainValue);
        dispatcher.addAudioProcessor(estimationGain);
    }

    public void addOnsetDetection(int size, double pickThreshold, double minInterOnsetInterv, double silenceThreshold) {
        dispatcher.addAudioProcessor(makeOnsetDetectorComplex(size, pickThreshold, minInterOnsetInterv, silenceThreshold));
        //dispatcher.addAudioProcessor(makeOnsetDetectorPercussion(size));
    }

    int k = 0;

    public void compute() throws Exception {
        Executors.callable(dispatcher).call();
    }

    /* */
    
    public VoiceDetection makePitchDetectionHandler(float sampleRate) {
        VoiceDetection prs = new VoiceDetection(sampleRate, settings.detection) {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                // Process
                float frequency = (float) computeFrequency(pitchDetectionResult);
                float amplitude = computeAmplitude(audioEvent);
                double timestamp = audioEvent.getTimeStamp();

                pitchEvents.add(SoundEvent.pitch((float) timestamp, frequency));
                ampliEvents.add(SoundEvent.amplitude((float) timestamp, amplitude));
            }
        };
        return prs;
    }
    
    public void handleOnsetEvent(double time, double salience) {
        onsetEvents.add(SoundEvent.onset((float)time, (float)salience));
        System.out.println(VoiceFileRead.class.getSimpleName() + " " + currentFile.getName() + " : Onset " + (k++) + "\ttime : " + time + "\tsalience : " + salience);
    }

    
    public ComplexOnsetDetector makeOnsetDetectorComplex(int size, double pickThreshold, double minInterOnsetInterv, double silenceThreshold) {
        ComplexOnsetDetector onsetDetector = new ComplexOnsetDetector(size, pickThreshold, minInterOnsetInterv, silenceThreshold);
        onsetDetector.setHandler(new OnsetHandler() {
            @Override
            public void handleOnset(double time, double salience) {
                handleOnsetEvent(time, salience);
            }
        });
        return onsetDetector;
    }
    
    public PercussionOnsetDetector makeOnsetDetectorPercussion(int size){
        PercussionOnsetDetector onsetDetector = new PercussionOnsetDetector(44100, size, new OnsetHandler() {
            @Override
            public void handleOnset(double time, double salience) {
                handleOnsetEvent(time, salience);
            }
        }, 70, 10);
        return onsetDetector;
    }
}
