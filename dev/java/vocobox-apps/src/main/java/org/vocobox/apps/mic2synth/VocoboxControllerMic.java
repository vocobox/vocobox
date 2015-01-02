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
        new VocoboxControllerMic();
    }
    
    /* ######################################################## */

    @Override
    public void wireVoice() {
        this.voice = new VoiceMicListen(synth);
    }

    @Override
    public void wireSynth() {
        this.synth = new JsynCircuitSynth();
    }

    @Override
    public void wireUI()  {
    	// settings
        this.monitorSettings = new MonitorSettings();
        this.monitorSettings.applyPalette = true;
        this.monitorSettings.timeMax = 60;
        
        // ui
        this.app = new VocoboxAppMic();
        this.panels = new VocoboxPanelsMic(this);
        this.app.layout(panels);
        this.monitor = panels.getSynthMonitors();    
    }
}
