/*
 *      _______                       _____   _____ _____  
 *     |__   __|                     |  __ \ / ____|  __ \ 
 *        | | __ _ _ __ ___  ___  ___| |  | | (___ | |__) |
 *        | |/ _` | '__/ __|/ _ \/ __| |  | |\___ \|  ___/ 
 *        | | (_| | |  \__ \ (_) \__ \ |__| |____) | |     
 *        |_|\__,_|_|  |___/\___/|___/_____/|_____/|_|     
 *                                                         
 * -------------------------------------------------------------
 *
 * TarsosDSP is developed by Joren Six at IPEM, University Ghent
 *  
 * -------------------------------------------------------------
 *
 *  Info: http://0110.be/tag/TarsosDSP
 *  Github: https://github.com/JorenSix/TarsosDSP
 *  Releases: http://0110.be/releases/TarsosDSP/
 *  
 *  TarsosDSP includes modified source code by various authors,
 *  for credits and info, see README.
 * 
 */

package org.vocobox.ui.audio.tarsos;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.vocobox.model.voice.analysis.VoiceAnalysisSettings;
import org.vocobox.voice.pitch.tarsos.VoiceInputStreamListen;

import be.tarsos.dsp.example.InputPanel;
import be.tarsos.dsp.example.PitchDetectionPanel;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class TarsosVocoPitchMicPanel extends JPanel {
    private static final long serialVersionUID = 3501426880288136245L;
    public VoiceInputStreamListen pitchMic;
    public JPanel inputPanel;
    public JPanel pitchDetectionPanel;
    public TarsosVocoPitchMicPanel(final VoiceInputStreamListen pitchMic) {
        this(pitchMic, false);
    }
    
    public TarsosVocoPitchMicPanel(final VoiceInputStreamListen pitchMic, boolean vertical) {
        if(!vertical)
        this.setLayout(new GridLayout(1, 0));
        else
            this.setLayout(new GridLayout(0, 1));

        this.pitchMic = pitchMic;
        pitchMic.settings.pitchDetectAlgo = VoiceAnalysisSettings.PITCH_DETECT_YIN;

        inputPanel = new InputPanel();
        inputPanel.setBorder(new TitledBorder("Microphones"));

        add(inputPanel);
        wireOnMixerChange(pitchMic, inputPanel);

        pitchDetectionPanel = new PitchDetectionPanel(algoChangeListener);
        pitchDetectionPanel.setBorder(new TitledBorder("Algorithms"));
        add(pitchDetectionPanel);
    }

    public void wireOnMixerChange(final VoiceInputStreamListen pitchMic, JPanel inputPanel) {
        inputPanel.addPropertyChangeListener("mixer", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                try {
                    pitchMic.changeMixer((Mixer) arg0.getNewValue());
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ActionListener algoChangeListener = new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
            String name = e.getActionCommand();
            PitchEstimationAlgorithm newAlgo = PitchEstimationAlgorithm.valueOf(name);
            pitchMic.settings.pitchDetectAlgo = newAlgo.toString();
            try {
                pitchMic.changeMixer(pitchMic.currentMixer);
            } catch (LineUnavailableException e1) {
                e1.printStackTrace();
            } catch (UnsupportedAudioFileException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    };
}
