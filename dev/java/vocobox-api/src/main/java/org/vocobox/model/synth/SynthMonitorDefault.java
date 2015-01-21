package org.vocobox.model.synth;

import org.vocobox.model.time.Timer;

public class SynthMonitorDefault extends Timer implements SynthMonitor {
    @Override
    public void amplitudeChanged(float value) {
        amplitudeChangeAt(elapsed(), value);
    }

    @Override
    public void pitchChanged(float value) {
        pitchChangeAt(elapsed(), value);
    }

    @Override
    public void pitchChanged(float value, Object info) {
        pitchChangeAt(elapsed(), value, info);
    }

    @Override
    public void pitchConfidenceChanged(float value) {
        pitchConfidenceChangedAt(elapsed(), value, 0);
    }

    @Override
    public void onsetOccured(float salience) {
        onsetOccuredAt(elapsed(), salience);
    }

    @Override
    public void offsetOccured() {
    }
    
    // ##################################################
    
    @Override
    public void pitchChangeAt(double time, float value, Object info) {
    }

    @Override
    public void pitchChangeAt(double time, float value) {
    }
    
    @Override
    public void pitchConfidenceChangedAt(double time, float confidence, float pitch) {
    }

    @Override
    public void onsetOccuredAt(double time, float salience) {
    }


    @Override
    public void midiVolume(int nChannel, int volume) {
    }

    @Override
    public void midiPitchBend(int nChannel, int value) {
    }

    @Override
    public void midiNoteOn(int nChannel, int nKey, int nVelocity) {
    }

    @Override
    public void midiNoteOff(int nChannel, int nKey) {
    }


    @Override
    public void amplitudeChangeAt(double time, float value) {
    }
}
