package org.vocobox.voice;

import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class VoiceAnalysisSettings {
    
    
    // tarsos global settings
    public PitchEstimationAlgorithm algo = PitchEstimationAlgorithm.YIN;
    public int bufferSize = 2048; // 1024 too small for lower notes
    public int overlap = 10; // mic listen=0, file read/play=1
    public int filterSize = 1; // default 5 : reducing to 1 reduce latency

    // envelope follower for smoother amplitude
    public boolean envelopeFollow = true;
    public double envelopeFollowAttackTime = 0.005;
    public double envelopeFollowReleaseTime = 0.05;
    
    
    
 // onset simple
    public float amplitudeThreshold = 0.05f;

    // onset v2
    public boolean onset = false;
    public double onsetPickThreshold = 0.15;
    public double onsetMinInterOnsetInterv= 0.25;
    public double onsetSilenceThreshold = -55;

    /**
     * 
     * @param peakThreshold A threshold used for peak picking. Values between 0.1 and 0.8. Default is 0.3, if too many onsets are detected adjust to 0.4 or 0.5.
     * @param silenceThreshold The threshold that defines when a buffer is silent. Default is -70dBSPL. -90 is also used.
     * @param minimumInterOnsetInterval The minimum inter-onset-interval in seconds. When two onsets are detected within this interval the last one does not count. Default is 0.004 seconds.
     */
    
    
    /*
     * 
     * 
     * double pickThreshold = 0.15;
    double minInterOnsetInterv= 0.25;
    double silenceThreshold = -55;*/
}
