/*
 *      _______                       _____   _____ _____  
 *     |__   __|                     |  __ \ / ____|  __ \ 
 *        | | __ _ _ __ ___  ___  ___| |  | | (___ | |__) |
 *        | |/ _` | '__/ __|/ _ \/ __| |  | |\___ \|  ___/ 
 *        | | (_| | |  \__ \ (_) \__ \ |__| |____) | |     
 *        |_|\__,_|_|  |___/\___/|___/_____/|_____/|_|     
 *                                                         
 * -------------------------------------------------------------
 *
 * TarsosDSP is developed by Joren Six at IPEM, University Ghent
 *  
 * -------------------------------------------------------------
 *
 *  Info: http://0110.be/tag/TarsosDSP
 *  Github: https://github.com/JorenSix/TarsosDSP
 *  Releases: http://0110.be/releases/TarsosDSP/
 *  
 *  TarsosDSP includes modified source code by various authors,
 *  for credits and info, see README.
 * 
 */

package org.vocobox.voice.pitch.tarsos.handler;

import org.vocobox.model.synth.VocoSynth;
import org.vocobox.model.voice.analysis.VoiceAnalysisSettings;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;

/**
 * Detect pitch, amplitude and set the synthetizer in turn
 * 
 */
public class VoiceDetectionSynthController extends VoiceDetection implements PitchDetectionHandler {
    protected VocoSynth synth;

    public VoiceDetectionSynthController(float samplerate, VoiceAnalysisSettings settings) {
        super(samplerate, settings);
    }

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        if (synth != null) {
            applyDetectionToSynth(pitchDetectionResult, audioEvent);
        } else {
            applyDetectionToSelfOscillo(pitchDetectionResult, audioEvent);
        }
    }
    
    public void applyDetectionToSynth(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        // Process
        float frequency = (float)computeFrequency(pitchDetectionResult);
        float amplitude = computeAmplitude(audioEvent);

        // Apply
        synth.sendFrequency(frequency);
        synth.sendAmplitude(amplitude);
        synth.sendConfidence(pitchDetectionResult.getProbability());
    }

    /* */

    public VocoSynth getSynth() {
        return synth;
    }

    public void setSynth(VocoSynth synth) {
        this.synth = synth;
    }
}
