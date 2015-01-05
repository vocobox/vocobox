package org.vocobox.voice.pitch.tarsos;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vocobox.model.synth.VocoSynth;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetection;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetectionSynthController;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchProcessor;

public class VoiceInputListen extends VoiceAnalyser {
    public AudioDispatcher dispatcher;
    public Mixer currentMixer;
    public VoiceDetectionSynthController pitchPitchHandler;
    public JVMAudioInputStream audioStream;
    
    public VoiceInputListen(){
    }
    
    public VoiceInputListen(VocoSynth synth){
        this.synth = synth;
    }

    public void setNewMixer(Mixer mixer) throws LineUnavailableException, UnsupportedAudioFileException, Exception {
        if (dispatcher != null) {
            dispatcher.stop();
        }
        currentMixer = mixer;
        settings.format = newAudioFormatWithSettings();
        configure(newAudioFormatWithSettings());
        run();
    }

    public void run() throws Exception {
        pitchPitchHandler.getSynth().on();
        new Thread(dispatcher, "Audio dispatching").start();
    }
    
    public VoiceDetection configure(AudioFormat format) throws LineUnavailableException {
        pitchPitchHandler = newPitchDetectionHandler(format.getSampleRate());
        audioStream = getAudioInputStream(currentMixer, format);
        dispatcher = newAudioDispatcher(audioStream);
        dispatcher.addAudioProcessor(new PitchProcessor(newPitchDetectAlgo(settings.pitchDetectAlgo), format.getSampleRate(), settings.bufferSize, pitchPitchHandler));
        return pitchPitchHandler;
    }


    public JVMAudioInputStream getAudioInputStream(Mixer mixer, final AudioFormat format) throws LineUnavailableException {
        // line
    	final DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine line = (TargetDataLine) mixer.getLine(dataLineInfo);
        line.open(format, settings.bufferSize);
        line.start();
        
        // Audio stream
        final AudioInputStream stream = new AudioInputStream(line);
        JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
        return audioStream;
    }
    
	public AudioFormat newAudioFormatWithSettings() {
	    final AudioFormat format = new AudioFormat(settings.sampleRate, settings.sampleSizeInBits, settings.channels, true, true);
	    return format;
    }

}
