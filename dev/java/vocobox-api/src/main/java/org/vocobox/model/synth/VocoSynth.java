package org.vocobox.model.synth;

import javax.swing.JPanel;

import org.vocobox.ui.charts.synth.SynthMonitorCharts;

/**
 * A synthetizer that should be controlled by voice parameter changes : frequency, amplitude, etc.
 */
public interface VocoSynth {
    // enable
    public void wire();
    public void on() throws Exception;
    public void off();
    
    // play
    public void sendAmplitude(float amplitude);
    public void sendFrequency(float frequency);
    public void sendConfidence(float confidence);
    public void sendOnset(float salience);
    public void sendOffset();
    
    // monitor
    public VocoSynthMonitor getMonitor();
    public void setMonitor(VocoSynthMonitor monitor);
    public SynthMonitorCharts getDefaultMonitor(MonitorSettings settings);
    
    // UI controller
    public JPanel newControlPanel();
}