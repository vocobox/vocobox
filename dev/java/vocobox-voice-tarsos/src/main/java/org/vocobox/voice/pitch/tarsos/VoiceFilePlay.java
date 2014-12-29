package org.vocobox.voice.pitch.tarsos;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vocobox.model.synth.VocoSynth;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetectionSynthController;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.GainProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

/** Schedule reading to trigger synthetizer commands on pitch change events. */
public class VoiceFilePlay extends VoiceTarsos {
    public AudioDispatcher estimationDispatcher;
    public AudioDispatcher sourceDispatcher;
    public GainProcessor estimationGain;
    public GainProcessor sourceGain;
    public File currentFile;

    public VoiceFilePlay() {
    }

    public VoiceFilePlay(VocoSynth synth) {
        this.synth = synth;
    }

    public void play(File file, double synthesisGainValue, double sourceGainValue) throws Exception {
        currentFile = file;
        AudioFormat format;
        format = AudioSystem.getAudioFileFormat(file).getFormat();

        if (format.getChannels() > 1) {
            throw new RuntimeException("Seems to be a stereo file! Interrupt because tarsos is very bad with stereo");
        }

        float samplerate = format.getSampleRate();
        int size = settings.bufferSize;// 1024;//1024;
        int overlap = settings.overlap;

        VoiceDetectionSynthController controller = configure(file, synthesisGainValue, sourceGainValue, format, samplerate, size, overlap);
        start(synthesisGainValue, sourceGainValue, controller);
    }

    public void start(double synthesisGainValue, double sourceGainValue, VoiceDetectionSynthController pitchDetectionHandler) throws Exception {
        if (pitchDetectionHandler.getSynth() != null)
            pitchDetectionHandler.getSynth().on();
        startSynthesis();
        // if (synthesisGainValue > 0) {
        // }
        if (sourceGainValue > 0) {
            startSourceFile();
        }
    }

    public void startSynthesis() {
        new Thread(estimationDispatcher).start();
    }

    public void startSourceFile() {
        new Thread(sourceDispatcher).start();
    }

    public VoiceDetectionSynthController configure(File file, double estimationGainValue, double sourceGainValue, AudioFormat format, float samplerate, int size, int overlap) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        VoiceDetectionSynthController pitchDetectionHandler = getVocoPitchDetectionHandler(samplerate);
        configureSynthesis(file, estimationGainValue, format, samplerate, size, overlap, pitchDetectionHandler);
        configureSource(file, sourceGainValue, format, size, overlap);
        return pitchDetectionHandler;
    }

    public void configureSource(File file, double sourceGainValue, AudioFormat format, int size, int overlap) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // To play source file
        sourceGain = new GainProcessor(sourceGainValue);
        sourceDispatcher = AudioDispatcherFactory.fromFile(file, size, overlap);
        sourceDispatcher.addAudioProcessor(sourceGain);
        sourceDispatcher.addAudioProcessor(new AudioPlayer(format));
    }

    public void configureSynthesis(File file, double estimationGainValue, AudioFormat format, float samplerate, int size, int overlap, VoiceDetectionSynthController pitchDetectionHandler) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // To run vocosynth & send to audio out input sound
        estimationGain = new GainProcessor(estimationGainValue);
        estimationDispatcher = AudioDispatcherFactory.fromFile(file, size, overlap);
        estimationDispatcher.addAudioProcessor(new PitchProcessor(settings.algo, samplerate, size, pitchDetectionHandler));
        estimationDispatcher.addAudioProcessor(estimationGain);

        // ONSET DETECT
        double threshold = 0.04;
        ComplexOnsetDetector onsetDetector = new ComplexOnsetDetector(size, threshold, 0.07, -60);
        onsetDetector.setHandler(new OnsetHandler() {
            int k = 0;
            @Override
            public void handleOnset(double arg0, double arg1) {
                System.out.println("Onset " + (k++) + " " + arg0 + " " + arg1);
            }
        });
        // add a processor, handle percussion event.
        estimationDispatcher.addAudioProcessor(onsetDetector);

        // WARN AUDIO player "schedule" la consommation du flux d'entrée
        // sans audio player, tous est calculé immédiatement, ce qui montre
        // la rapidité de l'algo
        estimationDispatcher.addAudioProcessor(new AudioPlayer(format));
    }
}
