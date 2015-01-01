package org.vocobox.model.voice.pitch;

import org.vocobox.model.synth.VocoSynth;

/**
 * A {@link VoiceController} implementation should control a {@link VocoSynth}
 * according to its input signal (microphone, wav file, audio signal)
 * 
 * @author Martin Pernollet
 */
public interface VoiceController {
    public VocoSynth getSynth();
    public void setSynth(VocoSynth synth);
}
