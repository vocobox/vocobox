package org.vocobox.model.voice.pitch;

import org.vocobox.model.synth.VocoSynth;

/**
 * A {@link VocoVoice} implementation should control a {@link VocoSynth}
 * according to the input signal (microphone, wav file, audio signal)
 * 
 * @author Martin Pernollet
 */
public interface VocoVoice {
    public VocoSynth getSynth();
    public void setSynth(VocoSynth synth);
}
