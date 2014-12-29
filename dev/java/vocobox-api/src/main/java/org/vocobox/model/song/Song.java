package org.vocobox.model.song;

import java.io.IOException;
import java.util.List;

import org.vocobox.events.On;
import org.vocobox.events.SoundEvent;
import org.vocobox.events.SoundEventReader;
import org.vocobox.events.policies.DefaultSoundEventPolicy;
import org.vocobox.events.policies.SoundEventPolicy;
import org.vocobox.model.synth.VocoSynth;

public class Song{
    public List<SoundEvent> envelope;
    public List<SoundEvent> pitch;
    public String folder;
    public String name;
    
    protected SoundEventPolicy policy = new DefaultSoundEventPolicy();

    public Song(String folder, String name) throws IOException{
        this.folder = folder;
        this.name = name;
        this.envelope = SoundEventReader.read(getFilename(folder, name, "envelope"), SoundEvent.Type.AMPLITUDE);
        this.pitch = SoundEventReader.read(getFilename(folder, name, "pitch"), SoundEvent.Type.PITCH);
    }
    
    public Song(String folder, String name, List<SoundEvent> pitch, List<SoundEvent> envelope) throws IOException{
        this.folder = folder;
        this.name = name;
        this.envelope = envelope;
        this.pitch = pitch;
    }
    
    public SoundEventPolicy getSoundEventPolicy() {
        return policy;
    }

    public void setSoundEventPolicy(SoundEventPolicy policy) {
        this.policy = policy;
    }


    public void preprocess(){
        policy.preprocessPitchEvents(pitch);
        policy.preprocessAmplitudeEvents(envelope);
    }

    public void play(final VocoSynth synth) throws Exception {
        policy.setSynth(synth);
        synth.on();
        policy.play(new On(){
            @Override
            public void finish() {
                synth.off();
            }
        });
    }
    
    //synthOff
    public void playMute(final VocoSynth synth) throws IOException {
        policy.setSynth(synth);
        synth.off();
        policy.play(new On(){
            @Override
            public void finish() {
                synth.off();
            }
        });
    }
    
    public String getFilename(String folder, String name, String info) {
        return folder + name + "/" + name + "-" + info + ".csv";
    }

    public String getExportFilename(String info, String ext) {
        return folder + name + "/" + name + "-" + info + "." + ext;
    }
    
    public int length(){
        return (int) Math.ceil(getLastEventTime());
    }
    
    public double getLastEventTime(){
        return Math.max(getLastAmplitudeEvent().timeInSec, getLastPitchEvent().timeInSec);
    }


    public SoundEvent getLastPitchEvent() {
        return pitch.get(pitch.size()-1);
    }


    public SoundEvent getLastAmplitudeEvent() {
        return envelope.get(envelope.size()-1);
    }


    public void stop() {
        policy.stop();
    }
}
