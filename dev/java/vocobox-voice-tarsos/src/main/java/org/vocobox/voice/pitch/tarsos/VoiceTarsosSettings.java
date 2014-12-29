package org.vocobox.voice.pitch.tarsos;

import org.vocobox.voice.pitch.tarsos.handler.DetectionSettings;

import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class VoiceTarsosSettings {
    public int bufferSize = 2048; // 1024 too small for lower notes
    public int overlap = 10; // mic listen=0, file read/play=1
    
    public PitchEstimationAlgorithm algo = PitchEstimationAlgorithm.YIN;

    public DetectionSettings detection = new DetectionSettings();
}
