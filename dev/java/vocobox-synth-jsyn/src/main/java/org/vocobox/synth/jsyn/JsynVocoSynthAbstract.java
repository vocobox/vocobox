package org.vocobox.synth.jsyn;

import java.io.IOException;

import org.vocobox.model.song.Song;
import org.vocobox.model.synth.MonitoredSynth;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.LinearRamp;
import com.jsyn.unitgen.UnitSource;

public abstract class JsynVocoSynthAbstract extends MonitoredSynth implements JSynVocoSynth{

    protected Synthesizer synth;
    protected LineOut lineOut;
    protected Song songData;
    protected boolean on = false;

    /**
     * We only need to start the LineOut. It will pull data from the oscillator.
     */
    @Override
    public void on() throws IOException {
        if(on)
            return;
        lineOut.start();
        if(monitor!=null)
            monitor.start();
        synth.start();
        on = true;
    }

    @Override
    public void off() {
        synth.stop();
        lineOut.stop();
        on = false;
    }
    
    public void wireToLineOut(UnitSource source){
        source.getOutput().connect(0, lineOut.input, 0);
        source.getOutput().connect(0, lineOut.input, 1);
    }

    @Override
    public Synthesizer getSynthesizer() {
        return synth;
    }

    @Override
    public LineOut getLineOut() {
        return lineOut;
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
}