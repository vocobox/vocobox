package org.vocobox.model.song;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vocobox.utils.EzRgx;

public class NoteParser extends EzRgx{
    public NoteParser(){
        pattern = Pattern.compile(FILE_PATTERN);
    }
    
    
    public boolean isNotePattern(File file) {
        return pattern.matcher(file.getName()).matches();
    }
    
    // + "(-" + numbers + ")*"
    static String p1 = "(" + letter + ")" + "(" + alteration + ")" + "(" + number + ")" + ".wav";
    static String p2 = "(" + letter + ")" + "(" + alteration + ")" + "(" + number + ")" + "(-" + numbers + "){0,1}" + ".wav";
    static String p3 = "(" + letter + ")" + "(" + alteration + ")" + "(" + number + ")" + "(-" + numbers + "){0,1}" + "(-" + word + "){0,1}" + ".wav";
    static String FILE_PATTERN = p3;
    protected Pattern pattern;

    public void parseNoteFileName(Note note, File file) {
        Matcher m = pattern.matcher(file.getName());
        if (m.matches()) {
            note.name = m.group(1).toLowerCase();
            note.sign = m.group(2);
            note.octave = Integer.parseInt(m.group(3));
            tryParseNum(note, m);
        } else {
            throw new IllegalArgumentException("unrecognize note file name pattern \nfor : " + file.getName() + " \npattern : " + FILE_PATTERN);
        }
    }

    public void tryParseNum(Note note, Matcher m) {
        try {
            note.instance = Integer.parseInt(m.group(4));
        } catch (Exception e) {
            note.instance = 1;
        }
    }
    
    public void setNameSignOctaveWithNoteString(Note note, String noteName) {
        note.name = (noteName.charAt(0)+"").toLowerCase();
        note.sign = 'b'==noteName.charAt(1)?"b":note.sign;
        note.sign = '#'==noteName.charAt(1)?"#":note.sign;
        note.octave = parseOctave(noteName);
    }

    protected int parseOctave(String noteName) {
        int i =-1;
        try{
            i = Integer.parseInt(noteName.charAt(1)+"");
        }
        catch(Exception e){
        }
        if(i==-1){
            try{
                i = Integer.parseInt(noteName.charAt(2)+"");
            }
            catch(Exception e){
            }
        }
        return i;
    }
    
    /* */
    
    
}
