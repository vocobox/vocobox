package org.vocobox.io.datasets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;
import org.vocobox.model.song.Note;
import org.vocobox.model.song.NoteParser;


public class HumanVoiceDataset {
    
    public static HumanVoiceDataset MARTIN = new HumanVoiceDataset("../../../../human-voice-dataset/data/", "voices/martin/notes/exports/mono/");
    
    protected String root;
    protected String dataset;

    public HumanVoiceDataset(String root, String dataset) {
        this.root = root;
        this.dataset = dataset;
    }
    
    public String[] getHeaders(){
        String[] headers = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
        return headers;
    }
    

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getFolder(){
        return root+dataset;
    }
    
    public Note getNote(String name){
        return new Note(name);
    }

    public String getNoteFilename(String name){
        String file = root + dataset + name;
        return file + ".wav";
    }
    
    public Note[][] getNoteMatrix() throws UnsupportedAudioFileException, IOException {
        Note[][] notes = new Note[6][12];
        List<Note> singleNotes = loadList(getFolder());
        
        for(Note n: singleNotes){
            if(n.orderStartAtA()<0)
                Logger.getLogger(HumanVoiceDataset.class).warn("Exclusing note " + n + " since order < 0");
            else
                notes[n.octave][n.orderStartAtA()] = n;
        }
        return notes;
    }
    
    public List<Note> loadList(String folder) throws UnsupportedAudioFileException, IOException {
        List<Note> notes = new ArrayList<Note>();
        File f = new File(folder);
        
        NoteParser p = new NoteParser();
        for (File n : f.listFiles()) {
            if (!p.isNotePattern(n))
                continue;
            Note note = new Note(n);
            System.out.println("done loading processing and building drawable model for " + note);
            notes.add(note);
        }
        return notes;
    }
}
