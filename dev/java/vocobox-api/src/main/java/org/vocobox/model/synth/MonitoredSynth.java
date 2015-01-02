package org.vocobox.model.synth;

import javax.swing.JPanel;

import org.vocobox.ui.charts.synth.SynthMonitorCharts;


public class MonitoredSynth implements VocoSynth{
	public SynthSettings settings = new SynthSettings();
    protected SynthMonitor monitor;

    public MonitoredSynth() {
    }
    
    public MonitoredSynth(SynthMonitor monitor) {
        this.monitor = monitor;
    }


    @Override
    public void wire() {
    }

    @Override
    public void on() throws Exception {
        if(monitor!=null)
            monitor.start();
    }

    @Override
    public void off() {
    }

    @Override
    public JPanel newControlPanel() {
        return null;
    }

    @Override
    public SynthMonitor getMonitor() {
        return monitor;
    }

    @Override
    public void setMonitor(SynthMonitor controlLog) {
        this.monitor = controlLog;
    }
    
    @Override
    public SynthMonitorCharts getDefaultMonitor(MonitorSettings settings)  {
        SynthMonitorCharts charts = new SynthMonitorCharts(settings);
        setMonitor(charts);
        return charts;
    }

    
    /* */

    @Override
    public void sendAmplitude(float amplitude) {
        amplitude = Math.min(amplitude, 1) * settings.amplitudeGain;
        
        doSetAmplitude(amplitude);
        if(monitor!=null)
            monitor.amplitudeChanged(amplitude);
    }

    @Override
    public void sendFrequency(float frequency) {
        doSetFrequency(frequency);
        if(monitor!=null)
            monitor.pitchChanged(frequency);
    }
    
    @Override
    public void sendConfidence(float confidence) {
        if(monitor!=null)
            monitor.pitchConfidenceChanged(confidence);
    }

    @Override
    public void sendOnset(float salience) {
        if(monitor!=null)
            monitor.onsetOccured(salience);
    }

    @Override
    public void sendOffset() {
        if(monitor!=null)
            monitor.offsetOccured();
    }

    /* */
    
    protected void doSetFrequency(float frequency) {
    }

    protected void doSetAmplitude(float amplitude) {
    }

}