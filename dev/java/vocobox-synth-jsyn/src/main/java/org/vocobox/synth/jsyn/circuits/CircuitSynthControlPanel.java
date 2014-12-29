package org.vocobox.synth.jsyn.circuits;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.jsyn.swing.SoundTweaker;

public class CircuitSynthControlPanel extends JPanel{
    private static final long serialVersionUID = -7228023739829300232L;
    public CircuitSynthControlPanel(){
    }
    
    public CircuitSynthControlPanel(JsynCircuitSynth synth){
        set(synth);
    }
    public void set(JsynCircuitSynth synth){
        setTweaker(synth);
    }

    public void setTweaker(JsynCircuitSynth synth) {
        SoundTweaker tweaker = new SoundTweaker(synth.getSynthesizer(), synth.getSource().getUnitGenerator().getClass().getName(), synth.getSource());
        add(tweaker, BorderLayout.CENTER);
    }
}
