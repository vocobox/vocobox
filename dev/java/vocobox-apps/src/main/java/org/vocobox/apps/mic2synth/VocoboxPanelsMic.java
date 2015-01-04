package org.vocobox.apps.mic2synth;

import javax.swing.JPanel;

import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.ui.audio.AudioSourceChart;
import org.vocobox.ui.audio.tarsos.TarsosVocoPitchMicPanel;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;
import org.vocobox.voice.pitch.tarsos.VoiceInputListen;

public class VocoboxPanelsMic {
    protected JPanel inputControl;
    protected JPanel synthControl;
    protected JPanel synthCharts;
    
    protected AudioSourceChart audioSourceCharts;
    protected SynthMonitorCharts synthMonitors;
    
    public VocoboxPanelsMic(VocoboxControllerMic controller) {
        buildPanels(controller, controller.monitorSettings);
        linkPanels(controller);
    }

    public void linkPanels(VocoboxControllerMic controller)  {
        inputControl = new TarsosVocoPitchMicPanel((VoiceInputListen) controller.voice);
        synthControl = controller.synth.newControlPanel();
        synthCharts = new MultiChartPanel(synthMonitors.getCharts());
    }

    public void buildPanels(VocoboxControllerMic controller, MonitorSettings settings)  {
        audioSourceCharts = new AudioSourceChart();
        //audioSourceCharts.makeCharts(vocoParse);
        synthMonitors = controller.synth.getDefaultMonitor(settings);
        synthMonitors.settings.timeMax = 100;
    }

    public JPanel getInputControl() {
        return inputControl;
    }

    public void setInputControl(JPanel inputControl) {
        this.inputControl = inputControl;
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
