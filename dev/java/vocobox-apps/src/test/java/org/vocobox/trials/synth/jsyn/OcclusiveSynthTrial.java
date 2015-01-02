package org.vocobox.trials.synth.jsyn;

import javax.media.opengl.GLProfile;

import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.events.policies.DefaultSoundEventPolicy;
import org.vocobox.model.song.Song;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.model.synth.VocoSynth;
import org.vocobox.synth.jsyn.dual.JsynOcclusiveNoiseSynth;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;

/**
 * Using {@link JsynOcclusiveNoiseSynth}, we can show low pitch detection confidence by playing
 * white noise instead of sound with a clearly defined frequency.
 * 
 * @author Martin Pernollet
 */
public class OcclusiveSynthTrial {
    public static int LENGTH = 35;

    public static void main(String[] args) throws Exception {
        GLProfile.get(GLProfile.GL2);
        // play("data/analyses/", "piano", 35);
        play("data/analyses/", "voice1", 15);
    }

    public static void play(String folder, String name, int length) throws Exception {
        MonitorSettings.DEFAULT.timeMax = length;

        VocoSynth synth = new JsynOcclusiveNoiseSynth();
        SynthMonitorCharts monitor = synth.getDefaultMonitor(MonitorSettings.DEFAULT);
        new MultiChartPanel(monitor.getCharts()).frame();
        
        // Play song to get logs
        Song song = new Song(folder, name);
        song.setSoundEventPolicy(new DefaultSoundEventPolicy());
        song.preprocess();
        song.play(synth);
    }
}
