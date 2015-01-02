package org.vocobox.apps;

import java.io.FileNotFoundException;

import org.vocobox.model.synth.JavaContext;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.model.synth.VocoSynth;
import org.vocobox.model.synth.VocoSynthMonitor;
import org.vocobox.model.voice.pitch.VoiceController;
import org.vocobox.synth.jsyn.JSynVocoSynth;
import org.vocobox.synth.jsyn.record.Recorder;

/**
 * USING JDK8 
 * provides 3 audio inputs
 * provides Gervill
 * 
 * WARNING : cause problem to load AWT on some demo (e.g. MIC)
 * 
 * USING JDK6 
 * provides one audio input with low sensitivity
 * provides Javasound Synth
 * 
 * 
 * set TarsosVocoPitchDetectionSynthController decay time to 0.05s to make less threshold crossing (higher value)
 * 
 * @author Martin
 */
public abstract class VocoboxControllerAbstract {
    public MonitorSettings monitorSettings;
    
    public VocoSynthMonitor monitor;
    public VocoSynth synth;
    public VoiceController voice;
    
    public VocoboxControllerAbstract() {
        wireSynth();
        wireVoice();
        wireUI();
    }
    
    public JavaContext getContext(){
        return new JavaContext.JDK6();
    }
    
    /**
     * Will export to file if the synthetizer is a {@link JSynVocoSynth}
     * @param filename
     * @return
     * @throws FileNotFoundException
     */
    public Recorder wireRecorder(String filename) throws FileNotFoundException{
        Recorder recorder = new Recorder((JSynVocoSynth)synth, filename);
        return recorder;
    }

    
    public abstract void wireSynth() ;
    public abstract void wireVoice() ;
    public abstract void wireUI();
    
	protected VoiceController getVoice() {
		return voice;
	}

}