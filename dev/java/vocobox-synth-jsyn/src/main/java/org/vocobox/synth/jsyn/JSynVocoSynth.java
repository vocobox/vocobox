package org.vocobox.synth.jsyn;

import org.vocobox.model.synth.VocoSynth;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitSource;

/**
 * Amplitude ports are set as a fractional Amplitude between -1.0 and +1.0
 * 
 * Taking min(input,1)
 */

public interface JSynVocoSynth extends VocoSynth{
    // circuit
    public Synthesizer getSynthesizer();
    public LineOut getLineOut();
    public UnitSource getSource();
    public UnitGenerator getGenerator();
    public UnitOutputPort getOutput();

}
