package org.vocobox.synth.jsyn.dual;

import org.vocobox.model.time.TimeUtils;
import org.vocobox.synth.jsyn.circuits.JsynCircuitSynth;

public class AbstractOcclusiveSynth extends JsynCircuitSynth {
    protected float balance = 1;
    protected double confidenceTimeWindow = 50;
    protected double octaveOffsetDetectionRatio = 1;
    protected float latestFrequency = -1;

    public enum Period{
        OCCLUSIVE,
        NOTE,
        ANY
    }

    /** computed internally according to relevance of command changes (pitch)
      *  -1 : occlusive synth
      *  1: note synth, 
      *  otherwise : all
      */
    public float getBalance() {
        return balance;
    }

    protected long latestTime = -1;

    public Period getPeriod() {
        if(balance==1){
            return Period.NOTE;
        }
        else if(balance==-1){
            return Period.OCCLUSIVE;
        }
        return Period.ANY;
    }

    public boolean inTimeWindow(long now) {
        return TimeUtils.diffNanoInMs(latestTime, now) < confidenceTimeWindow;
    }

    protected boolean hasMoreThanOctaveRange(float frq1, float frq2) {
        if (frq1 < frq2) {
            return frq2 > frq1 * 2 * octaveOffsetDetectionRatio;
        } else {
            return frq1 > frq2 * 2 * octaveOffsetDetectionRatio;
        }
    }

    public double getConfidenceTimeWindow() {
        return confidenceTimeWindow;
    }

    public void setConfidenceTimeWindow(double confidenceTimeWindow) {
        this.confidenceTimeWindow = confidenceTimeWindow;
    }

    public double getOctaveOffsetDetectionRatio() {
        return octaveOffsetDetectionRatio;
    }

    public void setOctaveOffsetDetectionRatio(double octaveOffsetDetectionRatio) {
        this.octaveOffsetDetectionRatio = octaveOffsetDetectionRatio;
    }
}