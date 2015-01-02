package org.vocobox.apps.csv2synth;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.bluecow.spectro.PlayerThread;

import org.vocobox.events.policies.MuteNoConfidencePolicy;
import org.vocobox.events.policies.SoundEventPolicy;
import org.vocobox.model.song.Song;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.model.synth.VocoSynth;
import org.vocobox.model.voice.analysis.VocoParseFileAndFFT;
import org.vocobox.synth.jsyn.circuits.JsynCircuitSynth;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;
import org.vocobox.ui.toolkit.transport.TransportPanel;
import org.vocobox.ui.toolkit.transport.TransportPanel.On;

/**
 * Control a synthetizer using CSV files : pitch.csv and envelope.csv, computed by
 * external software.
 */
public class VocoboxControllerCsv {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        VocoboxControllerCsv controller = new VocoboxControllerCsv();

        String analysisRoot = "data/analyses/";
        String analysisName = "doremi";
        String wavFile = "data/sound/" + analysisName + ".wav";
        controller.muteWinTime = 0.10f;// good for "piano" 0.15 // good for "dp2" 0.02
        controller.loadAnalysis(analysisRoot, analysisName, wavFile);
    }

    /* ######################################################## */

    // ui
    protected VocoboxAppCsv app;
    protected VocoboxPanelsCsv panels;
    protected SynthMonitorCharts monitor;

    // model
    protected VocoSynth synth;
    protected Song song;
    protected VocoParseFileAndFFT pitchAnalysis;
    protected SoundEventPolicy soundEventPolicy;
    
    // parameters
    protected float muteWinTime = -1f; // no mute based on confidence
    protected MonitorSettings monitorSettings;

    public VocoboxControllerCsv() {
        this(new VocoboxAppCsv(), new JsynCircuitSynth());
    }
    public VocoboxControllerCsv(VocoSynth synth) {
        this(new VocoboxAppCsv(), synth);
    }
    

    public VocoboxControllerCsv(VocoboxAppCsv app, VocoSynth synth) {
        this.app = app;
        this.synth = synth;
        
        this.monitorSettings = new MonitorSettings();
        this.monitorSettings.timeMax = 20;
    }

    /* EXTERNAL ANALYSIS FILES */
    
    public void loadAnalysis(String analysisRoot, String analysisName, String waveFile) throws IOException, UnsupportedAudioFileException {
        loadAnalysisData(analysisRoot, analysisName, waveFile);
        loadAnalysisUI();
                
    }

    public void loadAnalysisData(String analysisRoot, String analysisName, String waveFile) throws UnsupportedAudioFileException, IOException {
        pitchAnalysis = new VocoParseFileAndFFT(new File(waveFile));
        song = new Song(analysisRoot, analysisName);
    }

    public void loadAnalysisUI() throws IOException {
        panels = new VocoboxPanelsCsv(synth, song, pitchAnalysis, monitorSettings);
        panels.setInputControl(new TransportPanel(new On() {
            @Override
            public void play() {
                try {
                    VocoboxControllerCsv.this.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void stop() {
                VocoboxControllerCsv.this.stop();
                panels.getSynthMonitors().clear();
            }
        }));
        app.layout(panels);
        monitor = panels.getSynthMonitors();
        panels.getSynthMonitors().pitchCursorAnnotation.setWidth(muteWinTime);
    }

    /** Play song with synthetizer and source file at the same time */
    public void play() throws Exception {
        playAnalysisSource();
        playAnalysisSynthSong();
    }

    public void playAnalysisSource() {
        // devrait pas être attaché au syn monitor, mais séparé
        PlayerThread player = panels.getAudioSourceCharts().getClipPlayerThread();
        player.setPlaybackPosition(0);
        player.startPlaying();
    }

    public void playAnalysisSynthSong() throws Exception {
        preplayAnalysis();
        song.play(synth);
    }

    /**
     * Preprocess /filters sound events that would produce inappropriate synth
     * controls (uncertain pitch variations, etc)
     */
    public void preplayAnalysis() {
        soundEventPolicy = new MuteNoConfidencePolicy(monitor, muteWinTime);

        song.setSoundEventPolicy(soundEventPolicy);
        song.preprocess();
    }

    public void stop() {
        song.stop();
        synth.off();
        panels.getAudioSourceCharts().getClipPlayerThread().stopPlaying();
    }
}
