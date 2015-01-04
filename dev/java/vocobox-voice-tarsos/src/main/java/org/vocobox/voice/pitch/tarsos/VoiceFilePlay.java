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
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;

/** Schedule reading to trigger synthetizer commands on pitch change events. */
public class VoiceFilePlay extends VoiceAnalyser {
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

    public void play(File file) throws Exception {
        currentFile = file;
        settings.format = newAudioFormatWithSettings(file);//AudioSystem.getAudioFileFormat(file).getFormat();
        configure(settings.format);
        run();
    }

    /* RUN */
    
    public void run() throws Exception {
        if (synth != null)
            synth.on();
        runEstimationSynthesis();
        if (settings.sourceGainValue > 0) {
            runSourceFile();
        }
    }

    public void runEstimationSynthesis() {
        new Thread(estimationDispatcher).start();
    }

    public void runSourceFile() {
        new Thread(sourceDispatcher).start();
    }
    
    /* CONFIGURE */

	public VoiceDetectionSynthController configure(AudioFormat format) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        VoiceDetectionSynthController pitchDetectionHandler = newPitchDetectionHandler(format.getSampleRate());
        configureSynthesis(currentFile, format, pitchDetectionHandler);
        configureSource(currentFile, format);
        return pitchDetectionHandler;
    }

    public void configureSource(File file, AudioFormat format) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // To play source file
        sourceGain = new GainProcessor(settings.sourceGainValue);
        sourceDispatcher = newAudioDispatcher(file);
        sourceDispatcher.addAudioProcessor(sourceGain);
        sourceDispatcher.addAudioProcessor(new AudioPlayer(format));
    }

    /**
     * AudioPlayer used in this implementation is used to schedule reading of file and have
     * and approximative location of sound event. It is however prefered to use {@link VoiceFileRead} 
     * @param file
     * @param format
     * @param pitchDetectionHandler
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public void configureSynthesis(File file, AudioFormat format, VoiceDetectionSynthController pitchDetectionHandler) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // To run vocosynth & send to audio out input sound
        estimationGain = new GainProcessor(settings.estimationGainValue);
        estimationDispatcher = newAudioDispatcher(file);
        estimationDispatcher.addAudioProcessor(newPitchProcessor(format, pitchDetectionHandler));
        estimationDispatcher.addAudioProcessor(estimationGain);
        configureOnsetDetection();
        estimationDispatcher.addAudioProcessor(new AudioPlayer(format));
    }


	private void configureOnsetDetection() {
        ComplexOnsetDetector onsetDetector = makeComplexOnsetDetector();
        // add a processor, handle percussion event.
        estimationDispatcher.addAudioProcessor(onsetDetector);
    }

	private ComplexOnsetDetector makeComplexOnsetDetector() {
	    double threshold = 0.04;
        ComplexOnsetDetector onsetDetector = new ComplexOnsetDetector(settings.bufferSize, threshold, 0.07, -60);
        onsetDetector.setHandler(new OnsetHandler() {
            int k = 0;
            @Override
            public void handleOnset(double arg0, double arg1) {
                System.out.println("Onset " + (k++) + " " + arg0 + " " + arg1);
            }
        });
	    return onsetDetector;
    }
}
