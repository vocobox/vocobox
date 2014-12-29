package org.vocobox.synth.jsyn.dual;

import org.vocobox.model.synth.VocoSynth;
import org.vocobox.model.time.TimeUtils;

import com.jsyn.JSyn;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.WhiteNoise;

/**
 * A synthetizer mixing two base sound sources according to a level of
 * certainty/relevance in its pitch control
 */
public class JsynOcclusiveNoiseSynth extends AbstractOcclusiveSynth implements VocoSynth {
    protected WhiteNoise whiteNoise;

    // blaster circuit is unitgenerator
    // plugged to output as a unitsource

    @Override
    public void wire() {
        source = createCircuit();
        whiteNoise = new WhiteNoise();

        synth = JSyn.createSynthesizer();
        synth.add(lineOut = new LineOut());
        synth.add(whiteNoise.getUnitGenerator());
        synth.add(source.getUnitGenerator());

        // circuit
        wireToLineOut(whiteNoise);
        wireToLineOut(source);
        
        synth.add(frequencyRamp = makeFrequencyRamp(getCircuit().frequency()));
        synth.add(amplitudeRamp = makeAmplitudeRamp(getCircuit().amplitude()));
    }
    

 @Override
    public void sendFrequency(float frequency) {
        long now = TimeUtils.nowNano();

        if (inTimeWindow(now) && hasMoreThanOctaveRange(frequency, latestFrequency)) {
            balance = -1;
        } else {
            balance = 1;
        }

        latestFrequency = frequency;
        latestTime = now;

        // do usual stuff
        doSetFrequency(frequency);
        if(monitor!=null){
            monitor.pitchChanged(frequency);//, getPeriod());
        }
    }
    
    @Override
    public void doSetAmplitude(float amplitude){
        if(balance==1){
            doSetAmplitudeOcclusive(0);
            doSetAmplitudeNote(amplitude);
        }
        else if(balance==-1){
            doSetAmplitudeOcclusive(amplitude);
            doSetAmplitudeNote(0);
        }
        else{
            doSetAmplitudeOcclusive(amplitude);
            doSetAmplitudeNote(amplitude);            
        }
    }

    public void doSetAmplitudeOcclusive(float amplitude) {
        whiteNoise.amplitude.set(amplitude);
    }

    public void doSetAmplitudeNote(float amplitude) {
        if(amplitudeRamp!=null){
            amplitudeRamp.input.set(amplitude);
        }
        else{
            getCircuit().amplitude().set(amplitude);            
        }
    }
}
