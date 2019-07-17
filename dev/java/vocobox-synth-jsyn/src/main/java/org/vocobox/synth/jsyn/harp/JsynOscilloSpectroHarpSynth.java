package org.vocobox.synth.jsyn.harp;

import java.util.HashMap;
import java.util.Map;

import org.jzy3d.spectro.trials.SpectrumModelSpectro;
import org.vocobox.model.synth.SynthMonitor;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.LinearRamp;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;

public class JsynOscilloSpectroHarpSynth {
    // UnitOscillator oscillo;
    Map<Double, UnitOscillator> oscilloHarp;
    Map<Double, LinearRamp> oscilloAmplitudeHarp;
    protected Synthesizer synth;
    protected LineOut lineOut;
    protected SynthMonitor controlLog;

    protected SpectrumModelSpectro spectrum;
    protected double maxFreq;

    public JsynOscilloSpectroHarpSynth(SpectrumModelSpectro spectrum, double maxFreq) {
        this.spectrum = spectrum;
        this.synth = JSyn.createSynthesizer();
        this.synth.add(lineOut = new LineOut());
        this.maxFreq = maxFreq;
        this.oscilloHarp = new HashMap<Double, UnitOscillator>();
        this.oscilloAmplitudeHarp = new HashMap<Double, LinearRamp>();

        int k = 0;
        double[] frequencies = spectrum.getFrequencies();
        for (int i = 0; i < frequencies.length; i++) {
            double frequency = frequencies[i];
            if (frequency > maxFreq) {
                break;
            }

            UnitOscillator oscillator = makeOscillator();
            oscillator.frequency.set(frequency);
            oscillator.amplitude.set(0.0);
            oscilloHarp.put(frequency, oscillator);
            wireToLineOut(oscillator);
            synth.add(oscillator);
            k++;
            
            LinearRamp ramp = makeAmplitudeRamp(oscillator.amplitude);
            oscilloAmplitudeHarp.put(frequency, ramp);
            synth.add(ramp);
        }
        System.out.println(k + " oscillos");
    }
    

    public LinearRamp makeFrequencyRamp(UnitInputPort port) {
        return makeRamp(port, ramplen, 0, 1760, 440);
    }

    public LinearRamp makeAmplitudeRamp(UnitInputPort port) {
        return makeRamp(port, ramplen, 0, 1, 0.5);
    }
    
    protected double ramplen = 0.020; // 10 msec ramp for smoothing
    

    public LinearRamp makeRamp(UnitInputPort port, double duration, double min, double max, double val) {
        LinearRamp ramp = new LinearRamp();
        ramp.output.connect(port);
        ramp.input.setup(min, max, val);
        ramp.time.set(duration); 
        return ramp;
    }


    public void play() {
        lineOut.start();
        synth.start();

        Runnable r = new Runnable(){
            @Override
            public void run(){
                for (int i = 0; i < spectrum.getFrameCount(); i++) {
                    setSpectrumAt(i);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }      
            }
        };
        new Thread(r).start();
    }

    public void setSpectrumAt(int id) {
        double[] frequencies = spectrum.getFrequencies();

        int k = 0;
        for (int i = 0; i < frequencies.length; i++) {
            double frequency = frequencies[i];
            if (frequency > maxFreq) {
                break;
            }
            double e = spectrum.getEnergy(id, i);
            //oscilloHarp.get(frequency).amplitude.set(Math.abs(e*10));
            oscilloAmplitudeHarp.get(frequency).input.set(Math.abs(e*10));
            //System.out.println(e);
            k++;
        }
        //System.out.println(k + " oscillos");
    }

    public UnitOscillator makeOscillator() {
        return new SineOscillator();
    }

    public void wireToLineOut(UnitSource source) {
        source.getOutput().connect(0, lineOut.input, 0);
        source.getOutput().connect(0, lineOut.input, 1);
    }
}
