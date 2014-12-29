package org.vocobox.model.note;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vocobox.model.song.Note;
import org.vocobox.model.song.NoteParser;

public class MAPSNoteParser extends NoteParser{
    static String p = "MAPS_(" + word + ")_(" + word + ")_(" + letter + ")_(" + letter_and_nums + ")_M(" + numbers + ")_(" + letter_and_nums + ").wav";

    public MAPSNoteParser(){
        pattern = Pattern.compile(p);
    }
    
    @Override
    public void parseNoteFileName(Note n, File file) {
        MAPSNote note = (MAPSNote) n;
        Matcher m = pattern.matcher(file.getName());
        if (m.matches()) {
            note.type = m.group(1);
            note.playing = m.group(2);
            note.loudness = m.group(3);
            note.sustain = "S0".equals(m.group(4))?false:true;
            note.midiCode = Integer.parseInt(m.group(5));
            note.instrument = m.group(6);
            
            NoteDescriptor nv = NoteDescriptors.PIANO.getNoteByMidi(note.midiCode);
            String noteName = nv.name;
            setNameSignOctaveWithNoteString(note, noteName);
        } else {
            throw new IllegalArgumentException("unrecognize note file name pattern \nfor : " + file.getName() + " \npattern : " + pattern);
        }
    }
    
    @Override
    public boolean isNotePattern(File file) {
        //System.out.println(p);
        return pattern.matcher(file.getName()).matches();
    }
    
    
}
