package org.vocobox.events.policies;

import java.util.List;

import org.vocobox.events.On;
import org.vocobox.events.SoundEvent;
import org.vocobox.model.synth.VocoSynth;

public class DefaultSoundEventPolicy extends AbstractSoundEventPolicy {

    public DefaultSoundEventPolicy() {
        super();
    }

    public DefaultSoundEventPolicy(VocoSynth synth) {
        super(synth);
    }

    @Override
    public void onPitch(SoundEvent e) {
        synth.sendFrequency(e.value);
    }

    @Override
    public void onAmplitude(SoundEvent e) {
        float a = e.value;//getNormalizeAmplitude(e);
       // System.out.println(a);
        synth.sendAmplitude(a);
    }

    public float getNormalizeAmplitude(SoundEvent e) {
        //System.out.println("normalized amplitude : " + e.value + " max : " + amplitudeEventConsumer.getStatistics().max);
        return e.value / amplitudeEventConsumer.getStatistics().max;
    }

    /* */

    @Override
    public void play() {
        amplitudeEventConsumer.runnerStart();
        pitchEventConsumer.runnerStart();
    }

    @Override
    public void play(On finish) {
        amplitudeEventConsumer.runner(finish).start();
        pitchEventConsumer.runner(finish).start();
    }

    @Override
    public void preprocessPitchEvents(List<SoundEvent> events) {
        pitchEventConsumer = new SoundEventConsumer(events);
        pitchEventConsumer.addListener(this);
    }

    @Override
    public void preprocessAmplitudeEvents(List<SoundEvent> events) {
        amplitudeEventConsumer = new SoundEventConsumer(events);
        amplitudeEventConsumer.addListener(this);
        amplitudeEventConsumer.computeStatistics(events);
    }

    @Override
    public void stop() {
        amplitudeEventConsumer.runnerStop();
        pitchEventConsumer.runnerStop();
    }
}
