package org.vocobox.model.voice.pitch.evaluate;

import java.util.List;

import org.vocobox.events.SoundEvent;

/**
 * Should indicates the first time pitch sound events converge at a given distance of an expected frequency
 * @author Martin Pernollet
 *
 */
public interface PitchPrecisionLatency {
    public double latency(List<SoundEvent> commands, float expectedFrequency, float distance);
    public double distance(SoundEvent command, float expectedFrequency);
    
    public static double NO_CONVERGE = -0.1;
}
