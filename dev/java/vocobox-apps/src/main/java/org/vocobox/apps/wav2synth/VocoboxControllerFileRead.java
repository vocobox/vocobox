package org.vocobox.apps.wav2synth;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.primitives.axes.AxeXLineAnnotation;
import org.vocobox.apps.VocoboxControllerAbstract;
import org.vocobox.events.SoundEvent;
import org.vocobox.model.song.Song;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.synth.jsyn.circuits.JsynCircuitSynth;
import org.vocobox.synth.jsyn.record.Recorder;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;
import org.vocobox.ui.toolkit.transport.TransportPanel;
import org.vocobox.ui.toolkit.transport.TransportPanel.On;
import org.vocobox.voice.pitch.tarsos.VoiceFileRead;

/**
 * TODO : use pitch confidence to mute synth when no confidence. see VocoFileRead comments
 */
public class VocoboxControllerFileRead extends VocoboxControllerAbstract {
    public static void main(String[] args) throws Exception, UnsupportedAudioFileException {
        VocoboxControllerFileRead controller = new VocoboxControllerFileRead();
        String file = "data/sound/voice2-mono.wav";
        controller.monitorSettings.timeMax = 28;
        
        // Play and record
        Recorder recorder = controller.wireRecorder("target/out.wav");
        controller.play(file);
        recorder.recordFor(controller.monitorSettings.timeMax); // warn : blocking
    }

    protected VocoboxAppFile app;
    protected VocoboxPanelsFile panels;
    protected VoiceFileRead voice;
    protected String vocoFilename;
    protected Song song;

    @Override
    public void wireUI() {
        this.monitorSettings = new MonitorSettings();
        this.monitorSettings.timeMax = 12;
        this.app = new VocoboxAppFile();
    }

    @Override
    public void wireVoice() {
        this.voice = new VoiceFileRead();
    }

    /**
     * Synth know their own chart layout
     */
    @Override
    public void wireSynth() {
        this.synth = new JsynCircuitSynth();
        // this.synth = makeOcclusiveSynth(20, 1);
    }

    public void play(String file) throws Exception {
        loadFile(file);
        play();
    }

    /** Play song with synthetizer and source file at the same time */
    public void play() throws Exception {
        voice.read(new File(vocoFilename));
        song = new Song(null, null, voice.pitchEvents, voice.ampliEvents);
        song.preprocess();
        song.play(synth);

        showOnsets();

        // monit.ampliChart.getView().ge
    }

    public void showOnsets() {
        SynthMonitorCharts monit = (SynthMonitorCharts) monitor;
        // AxeBox axebox = (AxeBox) monit.ampliChart.getView().getAxe();
        showOnsetAsAxeAnnotation((AxeBox) monit.pitchChart.getView().getAxe());
        showOnsetAsAxeAnnotation((AxeBox) monit.ampliChart.getView().getAxe());
    }

    public void showOnsetAsAxeAnnotation(AxeBox axebox) {
        for (SoundEvent e : voice.onsetEvents) {
            AxeXLineAnnotation onset = new AxeXLineAnnotation();
            onset.setColor(Color.YELLOW);
            onset.setWidth(0.05f);
            onset.setValue(e.timeInSec);

            axebox.getAnnotations().add(onset);
        }
    }

    public void stop() {
        this.synth.off();
    }

    public void loadFile(String waveFile) {
        vocoFilename = waveFile;
        try {
            loadUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUI() throws IOException {
        panels = new VocoboxPanelsFile(synth, null, (int) this.monitorSettings.timeMax, monitorSettings);
        panels.setInputControl(new TransportPanel(new On() {
            @Override
            public void play() {
                try {
                    VocoboxControllerFileRead.this.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void stop() {
                VocoboxControllerFileRead.this.stop();
                panels.getSynthMonitors().clear();
            }
        }));
        app.layout(panels);
        monitor = panels.getSynthMonitors();
        // panels.getSynthMonitors().pitchCursorAnnotation.setWidth(muteWinTime);
    }
}
