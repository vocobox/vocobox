package org.vocobox.apps.wav2synth;

import java.io.IOException;

import javax.swing.JPanel;

import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.model.synth.VocoSynth;
import org.vocobox.model.voice.analysis.VocoParseFileAndFFT;
import org.vocobox.ui.audio.AudioSourceChart;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;

public class VocoboxPanelsFile {
    protected JPanel inputControl;
    protected JPanel inputCharts;
    protected JPanel synthControl;
    protected JPanel synthCharts;
    
    protected AudioSourceChart audioSourceCharts;
    protected SynthMonitorCharts synthMonitors;
    
    public VocoboxPanelsFile(VocoSynth synth, VocoParseFileAndFFT vocoParse, int length, MonitorSettings settings) throws IOException{
        buildPanels(synth, vocoParse, length, settings);
        linkPanels(synth);
    }

    public void linkPanels(VocoSynth synth) throws IOException {
        // input control
        //inputControl = audioSourceCharts.toolbarPanel;

        // input charts
        //inputCharts = audioSourceCharts.spectroPanel;
        //inputCharts = audioSourceCharts.spectroChart3dPanel;

        // synth control
        synthControl = synth.newControlPanel();
        
        // synth charts
        MultiChartPanel mSynthCharts = new MultiChartPanel(synthMonitors.getCharts());
        synthCharts = mSynthCharts;
    }

    public void buildPanels(VocoSynth synth, VocoParseFileAndFFT vocoParse, int length, MonitorSettings settings) throws IOException {
        //audioSourceCharts = new AudioSourceChart();
        //audioSourceCharts.makeCharts(vocoParse);
        synthMonitors = synth.getDefaultMonitor(settings);
        //length
    }

    public JPanel getInputControl() {
        return inputControl;
    }

    public void setInputControl(JPanel inputControl) {
        this.inputControl = inputControl;
    }

    public JPanel getInputCharts() {
        return inputCharts;
    }

    public void setInputCharts(JPanel inputCharts) {
        this.inputCharts = inputCharts;
    }

    public JPanel getSynthControl() {
        return synthControl;
    }

    public void setSynthControl(JPanel synthControl) {
        this.synthControl = synthControl;
    }

    public JPanel getSynthCharts() {
        return synthCharts;
    }

    public void setSynthCharts(JPanel synthCharts) {
        this.synthCharts = synthCharts;
    }

    public AudioSourceChart getAudioSourceCharts() {
        return audioSourceCharts;
    }

    public void setAudioSourceCharts(AudioSourceChart audioSourceCharts) {
        this.audioSourceCharts = audioSourceCharts;
    }

    public SynthMonitorCharts getSynthMonitors() {
        return synthMonitors;
    }

    public void setSynthMonitors(SynthMonitorCharts synthMonitors) {
        this.synthMonitors = synthMonitors;
    }
}
