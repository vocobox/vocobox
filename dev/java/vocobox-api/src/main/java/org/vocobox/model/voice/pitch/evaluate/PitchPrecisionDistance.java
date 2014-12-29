package org.vocobox.model.voice.pitch.evaluate;

public interface PitchPrecisionDistance {

    /**
     * 
     * Return the semitone distance between the estimated and expected frequency.
     * 
     * Example : considering those three notes
     * <ul>
     * <li>A4  : 440.00 Hz
     * <li>Ab4 : 415.30 Hz
     * <li>A#4 : 466.16 Hz
     * </ul>
     * 
     * Any frequency in the following range will be considered to be A4:
     * <ul>
     * <li>mean(freq(Ab4), freq(A4)) : A4 lower bound (also Ab4 upper bound)
     * <li>mean(freq(A4), freq(A#4)) : A4 upper bound (also A#4 lower bound)
     * </ul>
     * 
     * @param expectedFrequency
     * @param estimatedFrequency
     * @return
     */
    public double distance(float expectedFrequency, float estimatedFrequency);

}