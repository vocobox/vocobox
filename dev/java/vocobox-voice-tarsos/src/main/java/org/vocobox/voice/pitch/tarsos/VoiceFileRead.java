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
import org.vocobox.model.song.Song;
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
 * Read pitch change events from a file as fast as it can. Can't be used for
 * synthetizer control in live as the synthetizer would be controlled to fast.
 * 
 * TODO : should handle confidence in VoiceDetection instance
 */
public class VoiceFileRead extends VoiceAnalyser {
    public AudioDispatcher dispatcher;
    public AudioDispatcher sourceDispatcher;
    public GainProcessor estimationGain;
    public GainProcessor sourceGain;
    public File currentFile;

    public List<SoundEvent> pitchEvents = new ArrayList<SoundEvent>(1000);
    public List<SoundEvent> ampliEvents = new ArrayList<SoundEvent>(1000);
    public List<SoundEvent> onsetEvents = new ArrayList<SoundEvent>();

    /** return list of sound event, might not be ordered in time 
     * (mainly : ordered amplitude list appears AFTER ordered pitch list : not
     * interlaced)*/
    public List<SoundEvent> read(File file) throws Exception {
        clearEvents();
        currentFile = file;
        settings.format = newAudioFormatWithSettings(file);
        verifyFormat(settings.format);
        configure(settings.format);
        run();
        return getAllEvents();
    }
    
    /**
     * Return the file as a {@link Song}, a collection of pitch and amplitude change events 
     * which can easily be used to control a synthetizer:
     * @code song.play(synth);
     */
    public Song asSong() throws IOException{
		Song song = new Song(null, null, pitchEvents, ampliEvents);
		song.preprocess();
		return song;
    }


	public void clearEvents() {
	    pitchEvents.clear();
        ampliEvents.clear();
    }
    
    public void run() throws Exception {
        Executors.callable(dispatcher).call();
    }

    /* CONFIGURE PROCESSING */
    
    public void configure(AudioFormat format) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        dispatcher = AudioDispatcherFactory.fromFile(currentFile, settings.bufferSize, settings.overlap);
        addPitchDetection(format.getSampleRate());
        addGainProcessor(settings.estimationGainValue);
        if(settings.onset)
            addOnsetDetection();
    }

    public void addPitchDetection(float sampleRate) {
        VoiceDetection pitchDetectionHandler = makePitchDetectionHandler(sampleRate);
        dispatcher.addAudioProcessor(new PitchProcessor(newPitchDetectAlgo(settings.pitchDetectAlgo), sampleRate, settings.bufferSize, pitchDetectionHandler));
    }

    public void addGainProcessor(double estimationGainValue) {
        estimationGain = new GainProcessor(estimationGainValue);
        dispatcher.addAudioProcessor(estimationGain);
    }

    public void addOnsetDetection() {
        dispatcher.addAudioProcessor(makeOnsetDetectorComplex());
        //dispatcher.addAudioProcessor(makeOnsetDetectorPercussion(size));
    }



    /* PITCH DETECT */
    
    public VoiceDetection makePitchDetectionHandler(float sampleRate) {
        VoiceDetection prs = new VoiceDetection(sampleRate, settings) {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                // Process
                float frequency = (float) computeFrequency(pitchDetectionResult);
                float amplitude = computeAmplitude(audioEvent);
                double timestamp = audioEvent.getTimeStamp();

                // pitch
                SoundEvent pitch = SoundEvent.pitch((float) timestamp, frequency);
                pitch.confidence = pitchDetectionResult.getProbability();
                pitchEvents.add(pitch);

                // amplitude
                ampliEvents.add(SoundEvent.amplitude((float) timestamp, amplitude));
            }
        };
        return prs;
    }
    
    /* ONSET DETECT */
    
    public ComplexOnsetDetector makeOnsetDetectorComplex() {
        ComplexOnsetDetector onsetDetector = new ComplexOnsetDetector(settings.bufferSize, settings.onsetPickThreshold, settings.onsetMinInterOnsetInterv, settings.onsetSilenceThreshold);
        onsetDetector.setHandler(new OnsetHandler() {
            @Override
            public void handleOnset(double time, double salience) {
                doHandleOnsetEvent(time, salience);
            }
        });
        return onsetDetector;
    }
    
    public PercussionOnsetDetector makeOnsetDetectorPercussion(int size){
        PercussionOnsetDetector onsetDetector = new PercussionOnsetDetector(44100, size, new OnsetHandler() {
            @Override
            public void handleOnset(double time, double salience) {
                doHandleOnsetEvent(time, salience);
            }
        }, 70, 10);
        return onsetDetector;
    }
    
    protected void doHandleOnsetEvent(double time, double salience) {
        onsetEvents.add(SoundEvent.onset((float)time, (float)salience));
        System.out.println(VoiceFileRead.class.getSimpleName() + " " + currentFile.getName() + " : Onset " + (k++) + "\ttime : " + time + "\tsalience : " + salience);
    }
    
    int k = 0;

    
	private List<SoundEvent> getAllEvents() {
	    List<SoundEvent> all = new ArrayList<SoundEvent>(pitchEvents.size()+ampliEvents.size());
        all.addAll(pitchEvents);
        all.addAll(ampliEvents);
	    return all;
    }


}
