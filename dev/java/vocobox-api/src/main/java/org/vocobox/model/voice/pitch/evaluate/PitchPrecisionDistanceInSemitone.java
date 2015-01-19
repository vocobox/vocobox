package org.vocobox.model.voice.pitch.evaluate;

/**
 * Provides error functions to score pitch detection algorithms results
 * 
 * @author Martin Pernollet
 */
public class PitchPrecisionDistanceInSemitone implements PitchPrecisionDistance {
    public static final float NOTE_A4_FREQ = 440f;
    public static final int SEMITONES = 12;

    /**
     * 
     * Return the semitone distance between the estimated and expected
     * frequency.
     * 
     * Example : considering those three notes
     * <ul>
     * <li>A4 : 440.00 Hz
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
    @Override
    public double distance(float expectedFrequency, float estimatedFrequency) {
        return noteSemitoneValue(expectedFrequency) - noteSemitoneValue(estimatedFrequency);
    }

    public double noteSemitoneValue(float frequency) {
        return SEMITONES * log2(frequency / NOTE_A4_FREQ);
    }
    
    /** http://www.bibmath.net/dico/index.php3?action=affiche&quoi=./l/logarithme.html
     * http://gilles.costantini.pagesperso-orange.fr/Lycee_fichiers/CoursT_fichiers/ExpLn03.pdf
     * */
    /*public float semitoneValueToFrequency(){
        
    }*/

    /**
     * y=log2(x) <-> x=2^y
     */
    public double log2(float x) {
        return Math.log(x) / Math.log(2);
    }

    /**
     * y=log2(x) <-> x=2^y
     */
    public double log2Inverse(float y) {
        return Math.pow(2, y);
    }

    /**
     * Expect "clean" frequency as input, e.g. 440.0f for A4 Output : a lower
     * limit to define a range around input frequency
     */
    public int noteLowerBound(float frequency) {
        return 0;
    }

    public int noteUpperBound(float frequency) {
        return 0;
    }
}
