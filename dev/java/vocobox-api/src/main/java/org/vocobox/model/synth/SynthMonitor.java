package org.vocobox.model.synth;



public interface SynthMonitor {
    public void start();
    
    public void amplitudeChanged(float value);
    public void pitchChanged(float value);
    public void pitchChanged(float value, Object info);
    public void pitchConfidenceChanged(float value);
    public void onsetOccured(float salience);
    public void offsetOccured();
    
    public void amplitudeChangeAt(double time, float value);
    public void pitchChangeAt(double time, float value) ;
    public void pitchChangeAt(double time, float value, Object info);
    public void pitchConfidenceChangedAt(double time, float confidence, float pitch);
    public void onsetOccuredAt(double time, float salience);
    
    public void midiNoteOn(int channel, int key, int velocity);
    public void midiNoteOff(int channel, int key) ;
    public void midiPitchBend(int channel, int value);
    public void midiVolume(int channel, int volume);
}
