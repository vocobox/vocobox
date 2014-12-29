package org.vocobox.synth.jsyn.circuits;

import com.jsyn.ports.UnitInputPort;

public interface VocoSynthCircuitFacade {
    public UnitInputPort frequency();
    public UnitInputPort amplitude();
}
