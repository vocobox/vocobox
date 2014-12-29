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

public class VoiceMicListen extends VoiceTarsos {
    public AudioDispatcher dispatcher;
    public Mixer currentMixer;
    public VoiceDetectionSynthController vocoPitchHandler;
    public JVMAudioInputStream audioStream;
    
    /*public TarsosVocoPitchMic(){
        this(new JsynCircuitSynth());
    }*/
    public VoiceMicListen(){
    }
    
    public VoiceMicListen(VocoSynth synth){
        this.synth = synth;
    }

    public void setNewMixer(Mixer mixer) throws LineUnavailableException, UnsupportedAudioFileException, Exception {
        if (dispatcher != null) {
            dispatcher.stop();
        }
        currentMixer = mixer;

        // ----------------
        // AUDIO FORMAT
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 1;

        int bufferSize = settings.bufferSize; // was 1024
        int overlap = settings.overlap; // was 0
        final AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, true, true);
        
        // ----------------
        // WIRE STREAM
        wire(mixer, sampleRate, bufferSize, overlap, format);
        run();
    }

    public VoiceDetection wire(Mixer mixer, float sampleRate, int bufferSize, int overlap, final AudioFormat format) throws LineUnavailableException {
        vocoPitchHandler = getVocoPitchDetectionHandler(sampleRate);
        
        audioStream = getMicStream(mixer, bufferSize, format);
        dispatcher = new AudioDispatcher(audioStream, bufferSize, overlap);
        dispatcher.addAudioProcessor(new PitchProcessor(settings.algo, sampleRate, bufferSize, vocoPitchHandler));
        return vocoPitchHandler;
    }

    public void run() throws Exception {
        vocoPitchHandler.getSynth().on();
        new Thread(dispatcher, "Audio dispatching").start();
    }

    public JVMAudioInputStream getMicStream(Mixer mixer, int bufferSize, final AudioFormat format) throws LineUnavailableException {
        final DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine line = (TargetDataLine) mixer.getLine(dataLineInfo);
        final int numberOfSamples = bufferSize;
        line.open(format, numberOfSamples);
        line.start();
        final AudioInputStream stream = new AudioInputStream(line);
        JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
        return audioStream;
    }
}
