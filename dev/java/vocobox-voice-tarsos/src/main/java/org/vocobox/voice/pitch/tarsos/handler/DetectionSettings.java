package org.vocobox.voice.pitch.tarsos.handler;

public class DetectionSettings {
    public double envelopeFollowAttackTime = 0.005;
    public double envelopeFollowReleaseTime = 0.05;
    public boolean followEnvelope = true;
    public int filterSize = 1; // default 5 : reducing to 1 reduce latency
}
