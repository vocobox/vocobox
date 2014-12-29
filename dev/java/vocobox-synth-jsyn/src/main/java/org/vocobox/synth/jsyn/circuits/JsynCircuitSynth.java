package org.vocobox.synth.jsyn.circuits;

import org.vocobox.model.synth.VocoSynth;
import org.vocobox.synth.jsyn.JsynVocoSynthAbstract;

import com.jsyn.JSyn;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.LinearRamp;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitSource;

// return new com.syntona.exported.FMVoice();
// return new SubtractiveSynthVoice();
// return new WindCircuit();
public class JsynCircuitSynth extends JsynVocoSynthAbstract implements VocoSynth {
    public UnitSource source;
    public LinearRamp frequencyRamp;
    public LinearRamp amplitudeRamp;

    public JsynCircuitSynth() {
        wire();
    }

    @Override
    public void wire() {
        source = createCircuit();

        synth = JSyn.createSynthesizer();
        synth.add(lineOut = new LineOut());
        synth.add(source.getUnitGenerator());
        source.getOutput().connect(0, lineOut.input, 0);
        source.getOutput().connect(0, lineOut.input, 1);

        synth.add(frequencyRamp = makeFrequencyRamp(getCircuit().frequency()));
        synth.add(amplitudeRamp = makeAmplitudeRamp(getCircuit().amplitude()));
        //synth.start(); // calling start to early generates JVM crashes
    }
    
    public UnitSource createCircuit() {
        return new SynthCircuitBlaster();
    }
    
    public VocoSynthCircuitFacade getCircuit() {
        return (VocoSynthCircuitFacade)getGenerator();
    }
    @Override
    public void doSetAmplitude(float amplitude) {
        doSetAmplitudeOnRampOrCircuit(amplitude);
    }

    public void doSetAmplitudeOnRampOrCircuit(float amplitude) {
        if(amplitudeRamp!=null){
            amplitudeRamp.input.set(amplitude);
        }
        else{
            getCircuit().amplitude().set(amplitude);            
        }
    }


    @Override
    public void doSetFrequency(float frequency) {
        if(frequencyRamp!=null){
            frequencyRamp.input.set(frequency);
        }
        else{
            getCircuit().frequency().set(frequency);            
        }
    }
    
    @Override
    public CircuitSynthControlPanel newControlPanel() {
        return new CircuitSynthControlPanel(this);
    }
   
    @Override
    public UnitSource getSource() {
        return source;
    }
    
    @Override
    public UnitOutputPort getOutput(){
        return source.getOutput();
    }
    
    @Override
    public UnitGenerator getGenerator(){
        return source.getUnitGenerator();
    }
}
