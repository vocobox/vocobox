package org.vocobox.voice.pitch.tarsos.handler;

import java.util.Arrays;

import org.vocobox.model.voice.analysis.VoiceAnalysisSettings;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.EnvelopeFollower;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;

/**
 * Sample rate required for envelope computation. 
 * @author martin
 *
 */
public abstract class VoiceDetection implements PitchDetectionHandler{
    public VoiceAnalysisSettings settings = VoiceAnalysisSettings.DEFAULT;
    
    protected double prevFrequency = 0;
    protected final double[] previousFrequencies;
    protected int previousFrequencyIndex;

    protected EnvelopeFollower envelopeFollower;
    protected float sampleRate;

    protected double phase = 0;
    protected double phaseFirst = 0;
    protected double phaseSecond = 0;
    protected boolean usePureSine;

    public VoiceDetection(float sampleRate, VoiceAnalysisSettings settings){
        this(sampleRate, settings, false);
    }
    
    public VoiceDetection(float sampleRate, VoiceAnalysisSettings settings, boolean playPureSine) {
        super();
        this.usePureSine = playPureSine;
        this.sampleRate = sampleRate;
        this.envelopeFollower = new EnvelopeFollower(sampleRate, settings.envelopeFollowAttackTime, settings.envelopeFollowReleaseTime);//attack, release
        this.previousFrequencies = new double[settings.filterSize];
        this.previousFrequencyIndex = 0;
    }

    public double computeFrequency(PitchDetectionResult pitchDetectionResult) {
        double frequency = pitchDetectionResult.getPitch();
    
        if (frequency == -1) {
            frequency = prevFrequency;
        } else {
            if (previousFrequencies.length != 0) {
                // median filter
                // store and adjust pointer
                previousFrequencies[previousFrequencyIndex] = frequency;
                previousFrequencyIndex++;
                previousFrequencyIndex %= previousFrequencies.length;
                // sort to get median frequency
                double[] frequenciesCopy = previousFrequencies.clone();
                Arrays.sort(frequenciesCopy);
                // use the median as frequency
                frequency = frequenciesCopy[frequenciesCopy.length / 2];
            }
    
            prevFrequency = frequency;
        }
        return frequency;
    }

    public float computeAmplitude(AudioEvent audioEvent) {
        float[] env = computeEnvelope(audioEvent.getFloatBuffer());
        float amplitude = env[env.length - 1];
        return amplitude;
    }

    protected void computeEnvelopeAndApplyToBuffer(float[] audioBuffer, final double twoPiF) {
        float[] envelope = computeEnvelope(audioBuffer);
        applyEnvelopeToBuffer(audioBuffer, twoPiF, envelope);
    }

    protected float[] computeEnvelope(float[] audioBuffer) {
        float[] envelope = null;
        if (settings.envelopeFollow) {
            envelope = audioBuffer.clone();
            envelopeFollower.calculateEnvelope(envelope);
        }
        return envelope;
    }

    protected void computePhase(double timefactor) {
        phase = timefactor + phase;
        if (!usePureSine) {
            phaseFirst = 4 * timefactor + phaseFirst;
            phaseSecond = 8 * timefactor + phaseSecond;
        }
    }


    protected void applyEnvelopeToBuffer(float[] audioBuffer, final double twoPiF, float[] envelope) {
        for (int sample = 0; sample < audioBuffer.length; sample++) {
            double time = sample / sampleRate;
            double wave = Math.sin(twoPiF * time + phase);
            if (!usePureSine) {
                wave += 0.05 * Math.sin(twoPiF * 4 * time + phaseFirst);
                wave += 0.01 * Math.sin(twoPiF * 8 * time + phaseSecond);
            }
            audioBuffer[sample] = (float) wave;
            if (settings.envelopeFollow) {
                audioBuffer[sample] = audioBuffer[sample] * envelope[sample];
            }
        }
    }
    
    /* */
    
    protected void applyDetectionToSelfOscillo(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        double frequency = computeFrequency(pitchDetectionResult);
        final float[] audioBuffer = audioEvent.getFloatBuffer();
        final double twoPiF = 2 * Math.PI * frequency;
        double timefactor = twoPiF * audioBuffer.length / sampleRate;
        computeEnvelopeAndApplyToBuffer(audioBuffer, twoPiF);
        computePhase(timefactor);
    }

}