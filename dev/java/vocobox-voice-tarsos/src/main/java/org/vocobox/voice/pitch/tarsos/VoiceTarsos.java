package org.vocobox.voice.pitch.tarsos;

import org.vocobox.model.synth.VocoSynth;
import org.vocobox.model.voice.pitch.VocoVoice;
import org.vocobox.voice.pitch.tarsos.handler.VoiceDetectionSynthController;

public class VoiceTarsos implements VocoVoice{
    public VoiceTarsosSettings settings = new VoiceTarsosSettings();
    
    protected VocoSynth synth;
    
    public VoiceDetectionSynthController getVocoPitchDetectionHandler(float sampleRate) {
        VoiceDetectionSynthController prs = new VoiceDetectionSynthController(sampleRate, settings.detection);
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
