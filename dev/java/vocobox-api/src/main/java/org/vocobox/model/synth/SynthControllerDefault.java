package org.vocobox.model.synth;

public class SynthControllerDefault implements SynthController{
    protected VocoSynth synth;

    
    @Override
    public VocoSynth getSynth() {
        return synth;
    }

    @Override
    public void setSynth(VocoSynth synth) {
        this.synth = synth;
    }

}
