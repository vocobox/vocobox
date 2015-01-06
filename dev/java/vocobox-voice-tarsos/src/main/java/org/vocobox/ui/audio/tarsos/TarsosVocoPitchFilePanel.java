package org.vocobox.ui.audio.tarsos;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.vocobox.model.voice.analysis.VoiceAnalysisSettings;
import org.vocobox.voice.pitch.tarsos.VoiceFilePlay;

import be.tarsos.dsp.example.PitchDetectionPanel;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class TarsosVocoPitchFilePanel extends JPanel {
	private static final long serialVersionUID = 401554060116566946L;
	private final JSlider estimationGainSlider;
	private final JSlider sourceGainSlider;
	private final JFileChooser fileChooser;

	protected VoiceFilePlay pitchFile = new VoiceFilePlay();

	private ActionListener algoChangeListener = makeActionListener();

	public TarsosVocoPitchFilePanel() {
		this.setLayout(new BorderLayout());

		estimationGainSlider = new JSlider(0, 200);
		estimationGainSlider.setValue(100);
		estimationGainSlider.setPaintLabels(true);
		estimationGainSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (pitchFile.dispatcher != null) {
					double gainValue = estimationGainSlider.getValue() / 100.0;
					pitchFile.estimationGain.setGain(gainValue);
				}
			}
		});

		sourceGainSlider = new JSlider(0, 200);
		sourceGainSlider.setValue(100);
		sourceGainSlider.setPaintLabels(true);
		sourceGainSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (pitchFile.sourceDispatcher != null) {
					double gainValue = sourceGainSlider.getValue() / 100.0;
					pitchFile.sourceGain.setGain(gainValue);
				}
			}
		});

		JPanel fileChooserPanel = new JPanel(new BorderLayout());
		fileChooserPanel.setBorder(new TitledBorder("1. Choose your audio (wav mono)"));

		fileChooser = new JFileChooser();

		JButton chooseFileButton = new JButton("Choose a file...");
		chooseFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fileChooser.showOpenDialog(TarsosVocoPitchFilePanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						pitchFile.settings.estimationGainValue = estimationGainSlider.getValue() / 100.0;
						pitchFile.settings.sourceGainValue = sourceGainSlider.getValue() / 100.0;
						pitchFile.play(file);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// canceled
				}
			}
		});
		fileChooserPanel.add(chooseFileButton, BorderLayout.CENTER);

		JPanel gainPanel = new JPanel(new GridLayout(2, 2));
		JLabel label = new JLabel("Source gain (in %)");
		label.setToolTipText("Volume in % (100 is no change).");
		gainPanel.add(label);
		gainPanel.add(sourceGainSlider);
		label = new JLabel("Estimation Gain (in %)");
		label.setToolTipText("Volume in % (100 is no change).");
		gainPanel.add(label);
		gainPanel.add(estimationGainSlider);
		gainPanel.setBorder(new TitledBorder("3. Change the estimation / source"));
		this.add(fileChooserPanel, BorderLayout.NORTH);
		this.add(gainPanel, BorderLayout.SOUTH);
		JPanel pitchDetectionPanel = new PitchDetectionPanel(algoChangeListener);
		pitchFile.settings.pitchDetectAlgo = VoiceAnalysisSettings.PITCH_DETECT_YIN;
		this.add(pitchDetectionPanel, BorderLayout.CENTER);
	}

	public ActionListener makeActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				String name = e.getActionCommand();
				PitchEstimationAlgorithm newAlgo = PitchEstimationAlgorithm.valueOf(name);
				pitchFile.settings.pitchDetectAlgo = newAlgo.toString();
				if (pitchFile.currentFile != null) {
					pitchFile.dispatcher.stop();
					pitchFile.sourceDispatcher.stop();
					
					pitchFile.settings.estimationGainValue = estimationGainSlider.getValue() / 100.0;
					pitchFile.settings.sourceGainValue = sourceGainSlider.getValue() / 100.0;
					try {
						pitchFile.play(pitchFile.currentFile);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		};
	}
}