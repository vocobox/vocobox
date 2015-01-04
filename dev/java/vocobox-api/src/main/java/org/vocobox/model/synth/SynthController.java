package org.vocobox.model.synth;


/**
 * A {@link SynthController} implementation should control a {@link VocoSynth}
 * according to its input signal (microphone, wav file, audio signal)
 * 
 * @author Martin Pernollet
 */
public interface SynthController {
    public VocoSynth getSynth();
    public void setSynth(VocoSynth synth);
}
