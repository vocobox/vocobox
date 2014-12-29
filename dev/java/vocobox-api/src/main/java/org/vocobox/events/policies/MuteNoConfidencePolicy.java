package org.vocobox.events.policies;

import java.util.List;

import org.vocobox.events.SoundEvent;
import org.vocobox.maths.ConfidenceEvaluator;
import org.vocobox.model.synth.VocoSynthMonitor;

public class MuteNoConfidencePolicy extends DefaultSoundEventPolicy {
    float currentInputFreq;
    float currentInputAmpl;
    ConfidenceEvaluator confidence = new ConfidenceEvaluator();
    boolean muted = false;
    
    VocoSynthMonitor logger;
    float timeWindow;

    public MuteNoConfidencePolicy(VocoSynthMonitor logger) {
        this(logger, 0.1f);
    }
    public MuteNoConfidencePolicy(VocoSynthMonitor logger, float timeWindow) {
        super();
        this.logger = logger;
        this.timeWindow = timeWindow;
    }

    @Override
    public void preprocessPitchEvents(List<SoundEvent> events) {
        confidence.compute(events, timeWindow);
        //System.out.println("done computing confidence");

        pitchEventConsumer = new SoundEventConsumer(events);
        pitchEventConsumer.addListener(this);
    }

    @Override
    public void onPitch(SoundEvent e) {
        currentInputFreq = e.value;
        muted = e.confidence != 1;
        update();
        
        logger.pitchConfidenceChangedAt(e.timeInSec, e.confidence, e.value);
    }

    private void update() {
        if(muted){
            synth.sendAmplitude(0);
        }
        else{
            synth.sendFrequency(currentInputFreq);
            synth.sendAmplitude(currentInputAmpl);
        }
    }

    @Override
    public void onAmplitude(SoundEvent e) {
        currentInputAmpl = getNormalizeAmplitude(e);
        update();
    }

}
