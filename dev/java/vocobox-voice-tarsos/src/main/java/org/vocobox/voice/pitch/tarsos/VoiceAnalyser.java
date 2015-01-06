package org.vocobox.voice.pitch.tarsos;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vocobox.model.synth.SynthControllerDefault;
import org.vocobox.model.voice.analysis.VoiceAnalysisSettings;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetection;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetectionSynthController;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.onsets.PercussionOnsetDetector;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

/**
 * Should control synthetizer according voice analysis performed by
 * the actual implementation
 * 
 * @author Martin Pernollet
 */
public abstract class VoiceAnalyser extends SynthControllerDefault {
    public VoiceAnalysisSettings settings = VoiceAnalysisSettings.DEFAULT;

    /**
     * Should configure a {@link VoiceDetection} algorithm according to current
     * analyzer settings
     * @return
     * @throws Exception
     */
    public abstract VoiceDetection configure() throws Exception;

    /**
     * Run analysis on the content managed by this analyzer
     * @throws Exception
     */
    public abstract void run() throws Exception;

    
    public AudioDispatcher dispatcher;

	
	protected AudioFormat newAudioFormatWithSettings(File file) throws UnsupportedAudioFileException, IOException {
		AudioFormat format = AudioSystem.getAudioFileFormat(file).getFormat();
		verifyFormat(format);
        return format;
    }

	protected void verifyFormat(AudioFormat format) {
		if (format.getChannels() > 1) {
			if (settings.crashOnStereoFile) {
				throw new RuntimeException("Seems to be a stereo file! Interrupt because tarsos is very bad with stereo");
			} else {
				System.err.println(VoiceFileRead.class.getName() + " STEREO file not supported!");
			}
		}
	}

	protected AudioDispatcher newAudioDispatcher(JVMAudioInputStream audioStream) {
	    return new AudioDispatcher(audioStream, settings.bufferSize, settings.overlap);
    }

	protected AudioDispatcher newAudioDispatcher(File file) throws UnsupportedAudioFileException, IOException {
	    return AudioDispatcherFactory.fromFile(file, settings.bufferSize, settings.overlap);
    }
	
	protected PitchProcessor newPitchProcessor(AudioFormat format, VoiceDetectionSynthController pitchDetectionHandler) {
	    return new PitchProcessor(newPitchDetectAlgo(settings.pitchDetectAlgo), format.getSampleRate(), settings.bufferSize, pitchDetectionHandler);
    }
	
	protected PitchEstimationAlgorithm newPitchDetectAlgo(String name){
	    if(name!=null && name.contains("yin")){
	        return PitchEstimationAlgorithm.YIN;
	    }
	    else{
	        throw new IllegalArgumentException("unsupported algorithm " + name);
	    }
	}
	
	protected VoiceDetectionSynthController newPitchDetectionHandler(float sampleRate) {
		VoiceDetectionSynthController prs = new VoiceDetectionSynthController(sampleRate, settings);
		prs.setSynth(synth);
		return prs;
	}

    protected AudioFormat newAudioFormatWithSettings() {
        final AudioFormat format = new AudioFormat(settings.sampleRate, settings.sampleSizeInBits, settings.channels, true, true);
        return format;
    }

    protected JVMAudioInputStream newJVMAudioInputStream(Mixer mixer, final AudioFormat format) throws LineUnavailableException {
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
    
    protected ComplexOnsetDetector newOnsetDetectorComplex(OnsetHandler handler) {
        ComplexOnsetDetector onsetDetector = new ComplexOnsetDetector(settings.bufferSize, settings.onsetPickThreshold, settings.onsetMinInterOnsetInterv, settings.onsetSilenceThreshold);
        onsetDetector.setHandler(handler);
        return onsetDetector;
    }


    protected ComplexOnsetDetector newOnsetDetectorComplex(double threshold) {
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
    
    protected PercussionOnsetDetector newPercussionOnsetDetector(OnsetHandler handler) {
        return new PercussionOnsetDetector(settings.sampleRate, settings.bufferSize, handler, 70, 10);
    }
    
    
    
}
