package org.vocobox.apps.mic2synth;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.vocobox.apps.VocoboxControllerAbstract;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.synth.jsyn.circuits.JsynCircuitSynth;
import org.vocobox.voice.pitch.tarsos.VoiceMicListen;

public class VocoboxControllerMic extends VocoboxControllerAbstract{
    protected VocoboxAppMic app;
    protected VocoboxPanelsMic panels;

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        VocoboxControllerMic c = new VocoboxControllerMic();
    }
       
    @Override
    public void wireUI()  {
        this.monitorSettings = new MonitorSettings();
        monitorSettings.applyPalette = true;
        monitorSettings.timeMax = 60;
        this.app = new VocoboxAppMic();
        this.panels = new VocoboxPanelsMic(this);
        this.app.layout(panels);
        this.monitor = panels.getSynthMonitors();    
    }

    @Override
    public void wireVoice() {
        this.voice = new VoiceMicListen(synth);
    }

    @Override
    public void wireSynth() {
        this.synth = new JsynCircuitSynth();
    }
}
