package org.vocobox.voice.pitch.tarsos;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vocobox.model.synth.SynthControllerDefault;
import org.vocobox.voice.VoiceAnalysisSettings;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetectionSynthController;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchProcessor;

public class VoiceAnalyser extends SynthControllerDefault {
	public VoiceAnalysisSettings settings = new VoiceAnalysisSettings();

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
	    return new PitchProcessor(settings.algo, format.getSampleRate(), settings.bufferSize, pitchDetectionHandler);
    }
	
	protected VoiceDetectionSynthController newPitchDetectionHandler(float sampleRate) {
		VoiceDetectionSynthController prs = new VoiceDetectionSynthController(sampleRate, settings);
		prs.setSynth(synth);
		return prs;
	}


}
