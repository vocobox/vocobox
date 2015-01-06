package org.vocobox.voice.pitch.tarsos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

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

/**
 * Read pitch change events from a file as fast as it can. Can't be used for
 * synthetizer control in live as the synthetizer would be controlled to fast.
 * 
 * TODO : should handle confidence in VoiceDetection instance
 */
public class VoiceFileRead extends VoiceAnalyser {
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
        configure();
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
    
	@Override
    public void run() throws Exception {
        Executors.callable(dispatcher).call();
    }

	@Override
    public VoiceDetection configure() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        dispatcher = AudioDispatcherFactory.fromFile(currentFile, settings.bufferSize, settings.overlap);
        VoiceDetection d = addPitchDetection();
        addGainProcessor(settings.estimationGainValue);
        if(settings.onset)
            addOnsetDetection();
        return d;
    }

    public VoiceDetection addPitchDetection() {
        VoiceDetection pitchDetectionHandler = newPitchDetectionHandler();
        dispatcher.addAudioProcessor(new PitchProcessor(newPitchDetectAlgo(settings.pitchDetectAlgo), settings.format.getSampleRate(), settings.bufferSize, pitchDetectionHandler));
        return pitchDetectionHandler;
    }

    public void addGainProcessor(double estimationGainValue) {
        estimationGain = new GainProcessor(estimationGainValue);
        dispatcher.addAudioProcessor(estimationGain);
    }

    public void addOnsetDetection() {
        dispatcher.addAudioProcessor(newOnsetDetectorComplex());
        //dispatcher.addAudioProcessor(newOnsetDetectorPercussion());
    }
    
        
	protected List<SoundEvent> getAllEvents() {
	    List<SoundEvent> all = new ArrayList<SoundEvent>(pitchEvents.size()+ampliEvents.size());
        all.addAll(pitchEvents);
        all.addAll(ampliEvents);
	    return all;
    }
	
    /* FACTORY */
    
	/**
     * A pitch detection storing {@link SoundEvent}
     */
    protected VoiceDetection newPitchDetectionHandler() {
        VoiceDetection prs = new VoiceDetection(settings.format.getSampleRate(), settings) {
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
    
    protected ComplexOnsetDetector newOnsetDetectorComplex() {
        return newOnsetDetectorComplex(onsetHandlerDoer);
    }
    
    protected PercussionOnsetDetector newOnsetDetectorPercussion(OnsetHandler handler){
        return newPercussionOnsetDetector(onsetHandlerDoer);
    }

    OnsetHandler onsetHandlerDoer = new OnsetHandler() {
        @Override
        public void handleOnset(double time, double salience) {
            doHandleOnsetEvent(time, salience);
        }
    };
    
    protected void doHandleOnsetEvent(double time, double salience) {
        onsetEvents.add(SoundEvent.onset((float)time, (float)salience));
        System.out.println(VoiceFileRead.class.getSimpleName() + " " + currentFile.getName() + " : Onset " + (k++) + "\ttime : " + time + "\tsalience : " + salience);
    }
    
    int k = 0;

}
