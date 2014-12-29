package org.vocobox.model.voice.pitch.evaluate;

/**
 * Provides error functions to score pitch detection algorithms results
 * 
 * @author Martin Pernollet
 */
public class PitchPrecisionDistanceInFrequency implements PitchPrecisionDistance {
    /**
     * @return positive difference between expected and estimated frequency
     */
    @Override
    public double distance(float expectedFrequency, float estimatedFrequency){
        return Math.abs(expectedFrequency - estimatedFrequency);
    }
}
