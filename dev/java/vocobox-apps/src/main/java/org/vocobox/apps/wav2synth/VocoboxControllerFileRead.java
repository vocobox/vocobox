package org.vocobox.apps.wav2synth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.primitives.axes.AxeXLineAnnotation;
import org.vocobox.apps.VocoboxControllerAbstract;
import org.vocobox.events.SoundEvent;
import org.vocobox.model.song.Song;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.model.synth.MonitoredSynth;
import org.vocobox.synth.jsyn.circuits.JsynCircuitSynth;
import org.vocobox.synth.jsyn.record.Recorder;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;
import org.vocobox.ui.toolkit.transport.TransportPanel;
import org.vocobox.ui.toolkit.transport.TransportPanel.On;
import org.vocobox.voice.pitch.tarsos.VoiceFileRead;

/**
 * TODO : use pitch confidence to mute synth when no confidence. see
 * VoiceFileRead comments
 */
public class VocoboxControllerFileRead extends VocoboxControllerAbstract {
	public static void main(String[] args) throws Exception,
			UnsupportedAudioFileException {
		VocoboxControllerFileRead controller = new VocoboxControllerFileRead();
controller.play("data/sound/doremi-mono.wav");
		//controller.play("/Users/martin/Dev/vocobox/external/audio/coran/saint-coran-2min.wav");
        //exportCoran(controller);
		//exportVoice2(controller);
		//exportDoremi(controller);
		//exportPiano(controller);
	}
	
	/* ######################################################## */

	   public static void exportCoran(VocoboxControllerFileRead controller) throws FileNotFoundException, IOException, Exception{
	        String file ;
	        String out;
	        file = "/Users/martin/Dev/vocobox/external/audio/coran/saint-coran-10min.wav";
	        out = "target/saint-coran-out.wav";
	        controller.monitorSettings.timeMax = 60 * 10;
	        ((MonitoredSynth)controller.synth).settings.amplitudeGain = 2;
	        exportBlocking(controller, file, out);
	    }

	   
	public static void exportVoice2(VocoboxControllerFileRead controller) throws FileNotFoundException, IOException, Exception{
		String file ;
		String out;
		file = "data/sound/voice2-mono.wav";
		out = "target/voice2-out.wav";
		controller.monitorSettings.timeMax = 28;
		((MonitoredSynth)controller.synth).settings.amplitudeGain = 2;
		exportBlocking(controller, file, out);
	}

	public static void exportDoremi(VocoboxControllerFileRead controller) throws FileNotFoundException, IOException, Exception{
		String file ;
		String out;
		file = "data/sound/doremi-mono.wav";
		out = "target/doremi-out.wav";
		controller.monitorSettings.timeMax = 13;
		((MonitoredSynth)controller.synth).settings.amplitudeGain = 4;

		exportBlocking(controller, file, out);
	}

	public static void exportPiano(VocoboxControllerFileRead controller) throws FileNotFoundException, IOException, Exception{
		String file ;
		String out;
		file = "data/sound/piano-mono.wav";
		out = "target/piano-out.wav";
		controller.monitorSettings.timeMax = 31;
		((MonitoredSynth)controller.synth).settings.amplitudeGain = 10;

		exportBlocking(controller, file, out);
	}

	private static void exportBlocking(VocoboxControllerFileRead controller,
			String file, String out) throws FileNotFoundException, Exception,
			IOException {
		Recorder recorder = controller.wireRecorder(out);
		controller.play(file);
		recorder.recordFor(controller.monitorSettings.timeMax); 
	}

	/* ######################################################## */

	protected VocoboxAppFile app;
	protected VocoboxPanelsFile panels;
	protected String wavFile;
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

	@Override
	protected VoiceFileRead getVoice() {
		return (VoiceFileRead) voice;
	}

	/* ######################################################## */

	public void play(String file) throws Exception {
		loadFile(file);
		play();
	}

	/** Play song with synthetizer and source file at the same time */
	public void play() throws Exception {
		getVoice().read(new File(wavFile));
		song = new Song(null, null, getVoice().pitchEvents,
				getVoice().ampliEvents);
		song.preprocess();
		song.play(synth);

		showOnsets();
	}

	public void showOnsets() {
		SynthMonitorCharts monit = (SynthMonitorCharts) monitor;
		// AxeBox axebox = (AxeBox) monit.ampliChart.getView().getAxe();
		showOnsetAsAxeAnnotation((AxeBox) monit.pitchChart.getView().getAxe());
		showOnsetAsAxeAnnotation((AxeBox) monit.ampliChart.getView().getAxe());
	}

	public void showOnsetAsAxeAnnotation(AxeBox axebox) {
		for (SoundEvent e : getVoice().onsetEvents) {
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
		wavFile = waveFile;
		try {
			loadUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadUI() throws IOException {
		panels = new VocoboxPanelsFile(synth, null,
				(int) this.monitorSettings.timeMax, monitorSettings);
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
	}
}
