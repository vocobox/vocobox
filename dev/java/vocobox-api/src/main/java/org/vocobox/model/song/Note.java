package org.vocobox.model.song;

import java.io.File;

import org.vocobox.model.note.NoteDescriptor;
import org.vocobox.model.note.NoteDescriptors;
import org.vocobox.utils.EzRgx;

public class Note extends EzRgx {
    public static NoteParser NOTE_PARSER = new NoteParser();
    
    public String name;
    public String sign;
    public int octave;
    public int instance;
    public File file;

    public Note(){
    }
    
    public Note(String filename) {
        this(new File(filename));
    }

    public Note(File file) {
        this(file, NOTE_PARSER);
    }

    public Note(File file, NoteParser parser) {
        this.file = file;
        parser.parseNoteFileName(this, file);
    }
    
    public NoteDescriptor getDescriptor(){
        String fullName = (toString()).toUpperCase();
        return getDescriptor(fullName);
    }

    public NoteDescriptor getDescriptor(String fullName) {
        return NoteDescriptors.PIANO.getNoteByName(fullName);
    }


    /**
     * order of the note in its octave, where A as order 0.
     */
    public int orderStartAtA() {
        if ("a".equals(name)) {
            if("#".equals(sign)){
                return 1;
            }
            else if("b".equals(sign)){
                return 12;
            }
            else
                return 0;
        }
        else if ("b".equals(name)) {
            if("#".equals(sign)){
                return 3;
            }
            else if("b".equals(sign)){
                return 1;
            }
            else {
                return 2;
            }
        }
        else if ("c".equals(name)) {
            if("#".equals(sign)){
                return 4;
            }
            else if("b".equals(sign)){
                return 2;
            }
            else {
                return 3;
            }
        }
        else if ("d".equals(name)) {
            if("#".equals(sign)){
                return 6;
            }
            else if("b".equals(sign)){
                return 4;
            }
            else {
                return 5;
            }
        }
        else if ("e".equals(name)) {
            if("#".equals(sign)){
                return 8;
            }
            else if("b".equals(sign)){
                return 6;
            }
            else {
                return 7;
            }
        }
        else if ("f".equals(name)) {
            if("#".equals(sign)){
                return 9;
            }
            else if("b".equals(sign)){
                return 7;
            }
            else {
                return 8;
            }
        }
        else if ("g".equals(name)) {
            if("#".equals(sign)){
                return 11;
            }
            else if("b".equals(sign)){
                return 9;
            }
            else {
                return 10;
            }
        }
        return -1;
    }
    
    /**
     * order of the note in its octave, where A as order 0.
     */
    public int orderStartAtC() {
        if ("c".equals(name)) {
            if("#".equals(sign)){
                return 1;
            }
            else if("b".equals(sign)){
                return 12;
            }
            else
                return 0;
        }
        else if ("d".equals(name)) {
            if("#".equals(sign)){
                return 3;
            }
            else if("b".equals(sign)){
                return 1;
            }
            else {
                return 2;
            }
        }
        else if ("e".equals(name)) {
            if("#".equals(sign)){
                return 5;
            }
            else if("b".equals(sign)){
                return 3;
            }
            else {
                return 4;
            }
        }
        else if ("f".equals(name)) {
            if("#".equals(sign)){
                return 6;
            }
            else if("b".equals(sign)){
                return 4;
            }
            else {
                return 5;
            }
        }
        else if ("g".equals(name)) {
            if("#".equals(sign)){
                return 8;
            }
            else if("b".equals(sign)){
                return 6;
            }
            else {
                return 7;
            }
        }
        else if ("a".equals(name)) {
            if("#".equals(sign)){
                return 10;
            }
            else if("b".equals(sign)){
                return 8;
            }
            else {
                return 9;
            }
        }
        else if ("b".equals(name)) {
            if("#".equals(sign)){
                return 12;
            }
            else if("b".equals(sign)){
                return 10;
            }
            else {
                return 11;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return name + "" + sign + "" + octave;
    }

    /* */
    
}
