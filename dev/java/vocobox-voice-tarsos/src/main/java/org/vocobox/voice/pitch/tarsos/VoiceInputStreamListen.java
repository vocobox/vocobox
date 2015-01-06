package org.vocobox.voice.pitch.tarsos;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vocobox.model.synth.VocoSynth;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetection;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetectionSynthController;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchProcessor;

/**
 * Listen to currently selected source given by mixer and configured by settings.
 * 
 * @author Martin Pernollet
 */
public class VoiceInputStreamListen extends VoiceAnalyser {
    public AudioDispatcher dispatcher;
    public Mixer currentMixer;
    public VoiceDetectionSynthController pitchDetectionHandler;
    public JVMAudioInputStream audioStream;
    
    public VoiceInputStreamListen(){
    }
    
    public VoiceInputStreamListen(VocoSynth synth){
        this.synth = synth;
    }

    public void changeMixer(Mixer mixer) throws LineUnavailableException, UnsupportedAudioFileException, Exception {
        stopDispatcherIfAny();
        currentMixer = mixer;
        settings.format = newAudioFormatWithSettings();
        configure();
        run();
    }


    @Override
    public void run() throws Exception {
        pitchDetectionHandler.getSynth().on();
        new Thread(dispatcher, "Audio dispatching").start();
    }
    
    @Override
    public VoiceDetection configure() throws LineUnavailableException {
        pitchDetectionHandler = newPitchDetectionHandler(settings.format.getSampleRate());
        audioStream = newJVMAudioInputStream(currentMixer, settings.format);
        dispatcher = newAudioDispatcher(audioStream);
        dispatcher.addAudioProcessor(new PitchProcessor(newPitchDetectAlgo(settings.pitchDetectAlgo), settings.format.getSampleRate(), settings.bufferSize, pitchDetectionHandler));
        return pitchDetectionHandler;
    }
}
