package org.vocobox.model.note;

import java.io.File;

import org.vocobox.model.song.Note;
import org.vocobox.model.song.NoteParser;

/**
 * @see {@link NoteDescriptors} holds an array of 88 note description with name, frequency, etc
 * 
 * @author Martin Pernollet
 *
 */
public class NoteDescriptor {
    /** Note name (G#3, Ab5, etc) */
    final public String name;
    /** Alternative name of a note (G#=Ab) */
    final public String alternativeName;
    /** Note frequency*/
    final public double frequency;
    /** Piano Key id */
    final public int key;
    /** Midi note code */
    final public int midi;
    /** Provides detailed information of name , sign (#/b), octave number.*/
    final public Note note = new Note();
    
    public NoteDescriptor(String name, double freq, int key) {
        this.name = name;
        this.frequency = freq;
        this.key = key;
        this.midi = key + 20;
        this.alternativeName = null;
        parseNoteName(name, note);
    }

    public NoteDescriptor(String name, String alternate, double freq, int key) {
        this.name = name;
        this.alternativeName = alternate;
        this.frequency = freq;
        this.key = key;
        this.midi = key + 20;
        parseNoteName(name, note);
    }

    public void parseNoteName(String nameIn, Note noteOut) {
        NoteParser p = new NoteParser();
        p.parseNoteFileName(noteOut, new File(nameIn + ".wav"));
    }
    
    public float frequency(){
        return (float) frequency;
    }
}