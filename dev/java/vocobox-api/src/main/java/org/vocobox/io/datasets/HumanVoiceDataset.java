package org.vocobox.io.datasets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;
import org.vocobox.model.note.Note;
import org.vocobox.model.note.NoteParser;
import org.vocobox.model.note.Voyel;


public class HumanVoiceDataset {
    public static void configureDatasetRoot(String root){
        NOTES = new HumanVoiceDataset(root, "voices/martin/notes/exports/mono/");
        VOYELS = new HumanVoiceDataset(root, "voices/martin/voyels/exports/mono/");
    
        System.out.println("Set dataset root to : " + root);
    }
    
    public static HumanVoiceDataset NOTES = new HumanVoiceDataset("../../../../human-voice-dataset/data/", "voices/martin/notes/exports/mono/");
    public static HumanVoiceDataset VOYELS = new HumanVoiceDataset("../../../../human-voice-dataset/data/", "voices/martin/voyels/exports/mono/");
    
    protected String root;
    protected String dataset;

    public HumanVoiceDataset(String root, String dataset) {
        this.root = root;
        this.dataset = dataset;
    }
    
    public String[] getNoteHeaders(){
        String[] headers = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
        return headers;
    }

    public String[] getVoyelHeaders(){
        String[] headers = {Voyel.A.toString(), Voyel.E.toString(), Voyel.I.toString(), Voyel.O.toString(), Voyel.OU.toString(), Voyel.U.toString()};
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

    public File getNoteFile(String name){
        return new File(getNoteFilename(name));
    }


    
    public Note[][] getNoteMatrix() throws UnsupportedAudioFileException, IOException {
        Note[][] notes = new Note[6][12];
        List<Note> singleNotes = getNotes(getFolder());
        
        for(Note n: singleNotes){
            if(n.orderStartAtA()<0)
                Logger.getLogger(HumanVoiceDataset.class).warn("Exclusing note " + n + " since order < 0");
            else
                notes[n.octave][n.orderStartAtA()] = n;
        }
        return notes;
    }

    public Note[][] getVoyelRow() throws UnsupportedAudioFileException, IOException {
        Note[][] notes = new Note[6][12];
        List<Note> singleNotes = getNotes(getFolder());
        
        for(Note n: singleNotes){
            if(n.orderStartAtA()<0)
                Logger.getLogger(HumanVoiceDataset.class).warn("Exclusing note " + n + " since order < 0");
            else
                notes[n.octave][n.orderStartAtA()] = n;
        }
        return notes;
    }

    public List<Note> getNotes() throws UnsupportedAudioFileException, IOException {
        return getNotes(getFolder());
    }
    
    public List<Note> getNotes(String folder) throws UnsupportedAudioFileException, IOException {
        List<Note> notes = new ArrayList<Note>();
        File f = new File(folder);
        
        System.out.println(this.getClass().getSimpleName() + " get notes from folder " + folder);
        
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
