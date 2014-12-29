package org.vocobox.trials.synth.jsyn;
import org.vocobox.events.policies.DefaultSoundEventPolicy;
import org.vocobox.model.song.Song;
import org.vocobox.synth.jsyn.JSynVocoSynth;
import org.vocobox.synth.jsyn.monoscillo.JsynMonoscilloSynth;
import org.vocobox.synth.jsyn.record.Recorder;

/**
 * Les event d'amplitude sont très nombreux. 
 * A l'origine des clicks : utiliser une ramp
 * 
 */
public class MonoscilloRecordTrial {
    public static void main(String[] args) throws Exception {
        play("data/analyses/","doremi");
        //play("data/analyses/","piano");
        //play("data/analyses/", "voice1");
    }
    
    public static void play(String folder, String name) throws Exception{
        Song song = new Song(folder, name);
        JSynVocoSynth synth = new JsynMonoscilloSynth();
        Recorder recorder = new Recorder(synth, song.getExportFilename("osc", "wav"));
        
        song.setSoundEventPolicy(new DefaultSoundEventPolicy());
//        song.setControlEventPolicy(new MuteNoConfidencePolicy());
        song.preprocess();
        song.play(synth);
        recorder.recordFor(song.getLastEventTime()+1);
    }    
}
