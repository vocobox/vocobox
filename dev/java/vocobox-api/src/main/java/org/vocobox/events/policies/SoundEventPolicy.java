package org.vocobox.events.policies;

import java.util.List;

import org.vocobox.events.On;
import org.vocobox.events.SoundEvent;
import org.vocobox.model.synth.VocoSynth;

public interface SoundEventPolicy {
    public void setSynth(VocoSynth synth);
    
    public void play();
    public void play(On listener);
    public void onPitch(SoundEvent e);
    public void onAmplitude(SoundEvent e);

//    public void reset();
    
    public void preprocessPitchEvents(List<SoundEvent> events);
    public void preprocessAmplitudeEvents(List<SoundEvent> events);
    
    public abstract void event(SoundEvent e);
    public abstract boolean isPitch(SoundEvent e);
    public abstract boolean isAmplitude(SoundEvent e);

    public SoundEventConsumer getPitchEventConsumer();
    public SoundEventConsumer getAmplitudeEventConsumer();

    public void stop();
}