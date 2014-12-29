package org.vocobox.events.policies;

import java.util.List;

import org.vocobox.events.SoundEvent;
import org.vocobox.events.listeners.SoundEventListener;
import org.vocobox.model.synth.VocoSynth;

public abstract class AbstractSoundEventPolicy implements SoundEventListener, SoundEventPolicy{
    @Override
    public abstract void onPitch(SoundEvent e);
    @Override
    public abstract void onAmplitude(SoundEvent e);
    @Override
    public abstract void preprocessPitchEvents(List<SoundEvent> events);

    protected SoundEventConsumer pitchEventConsumer;
    protected SoundEventConsumer amplitudeEventConsumer;
    protected VocoSynth synth;

    public AbstractSoundEventPolicy() {
    }

    public AbstractSoundEventPolicy(VocoSynth synth) {
        this.synth = synth;
    }
    
    @Override
    public void event(SoundEvent e) {
        if(isPitch(e)){
            onPitch(e);
        }
        else if(isAmplitude(e)){
            onAmplitude(e);
        }
    }

    @Override
    public boolean isPitch(SoundEvent e) {
        return SoundEvent.Type.PITCH.equals(e.type);
    }
    
    @Override
    public boolean isAmplitude(SoundEvent e) {
        return SoundEvent.Type.AMPLITUDE.equals(e.type);
    }
    

    @Override
    public void setSynth(VocoSynth synth) {
        this.synth = synth;
    }
    
    @Override
    public SoundEventConsumer getPitchEventConsumer() {
        return pitchEventConsumer;
    }
    
    @Override
    public SoundEventConsumer getAmplitudeEventConsumer() {
        return amplitudeEventConsumer;
    }

}
