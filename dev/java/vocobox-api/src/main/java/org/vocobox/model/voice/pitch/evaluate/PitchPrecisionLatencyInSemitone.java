package org.vocobox.model.voice.pitch.evaluate;

import java.util.List;

import org.vocobox.events.SoundEvent;

public class PitchPrecisionLatencyInSemitone implements PitchPrecisionLatency {
    PitchPrecisionDistanceInSemitone semitoneDistance = new PitchPrecisionDistanceInSemitone();
    
    public PitchPrecisionLatencyInSemitone() {
    }
    
    /**
     * Commands must be ordered in time
     * 
     * Return {@link PitchPrecisionLatency.NO_CONVERGE} if pitch command did not when close enough
     * to expected frequency.
     */
    @Override
    public double latency(List<SoundEvent> commands, float expectedFrequency, float distance) {
        for(SoundEvent e: commands){
            if(distance(e, expectedFrequency)<=distance){
                return e.timeInSec;
            }
        }
        return NO_CONVERGE;
    }

    @Override
    public double distance(SoundEvent command, float expectedFrequency) {
        return Math.abs(semitoneDistance.distance(expectedFrequency, command.value));
    }
}
