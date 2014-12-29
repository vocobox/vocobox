package org.vocobox.apps;

import org.vocobox.model.synth.JavaContext;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.model.synth.VocoSynth;
import org.vocobox.model.synth.VocoSynthMonitor;
import org.vocobox.model.voice.pitch.VocoVoice;

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
    public VocoVoice voice;
    
    public VocoboxControllerAbstract() {
        wireSynth();
        wireVoice();
        wireUI();
    }
    
    public JavaContext getContext(){
        return new JavaContext.JDK6();
    }

    
    public abstract void wireSynth() ;
    public abstract void wireVoice() ;
    public abstract void wireUI();
}