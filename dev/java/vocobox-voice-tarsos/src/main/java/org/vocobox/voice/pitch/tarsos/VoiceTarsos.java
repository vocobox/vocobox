package org.vocobox.voice.pitch.tarsos;

import org.vocobox.model.synth.VocoSynth;
import org.vocobox.model.voice.pitch.VoiceController;
import org.vocobox.voice.VoiceAnalysisSettings;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetectionSynthController;

public class VoiceTarsos implements VoiceController{
    public VoiceAnalysisSettings settings = new VoiceAnalysisSettings();
    
    protected VocoSynth synth;
    
    public VoiceDetectionSynthController getVocoPitchDetectionHandler(float sampleRate) {
        VoiceDetectionSynthController prs = new VoiceDetectionSynthController(sampleRate, settings);
        prs.setSynth(synth);
        return prs;
    }

    @Override
    public VocoSynth getSynth() {
        return synth;
    }

    @Override
    public void setSynth(VocoSynth synth) {
        this.synth = synth;
    }

}
