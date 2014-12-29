package org.vocobox.model.note;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vocobox.io.EzRgx;

public class NoteParser extends EzRgx{
    static EzRgx RGX = newRgx().optionTwoLetters("_-").note().optionNumbers().optionWord().extWav();
    
    protected Pattern pattern;

    public NoteParser(){
        pattern = RGX.compilePattern();
    }
    
    public boolean isNotePattern(File file) {
        return pattern.matcher(file.getName()).matches();
    }

    public void parseNoteFileName(Note note, File file) {
        Matcher m = pattern.matcher(file.getName());
        if (m.matches()) {
            parseNoteVoyel(note, m, 1);
            parseNoteName(note, m, 2);
            parseNoteSign(note, m, 3);
            parseOctave(note, m, 4);
            parseInstance(note, m, 5);
        } else {
            throw new IllegalArgumentException("unrecognize note file name pattern \nfor : " + file.getName() + " \npattern : " + RGX.getPattern());
        }
    }

    protected void parseNoteVoyel(Note note, Matcher m, int groupId) {
        if(m.group(1)!=null){
            int i = m.group(1).indexOf('-');
            int j = m.group(1).lastIndexOf('-');
            note.voyel = Voyel.parse(m.group(1).substring(i+1, j));
        }
    }

    protected void parseNoteName(Note note, Matcher m, int groupId) {
        note.name = m.group(groupId).toLowerCase();
    }
    
    protected void parseNoteSign(Note note, Matcher m, int groupId) {
        note.sign = m.group(groupId);
    }

    protected void parseInstance(Note note, Matcher m, int groupId) {
        try {
            note.instance = Integer.parseInt(m.group(groupId));
        } catch (Exception e) {
            note.instance = 1;
        }
    }
    
    protected void parseOctave(Note note, Matcher m, int groupId) {
        note.octave = Integer.parseInt(m.group(groupId));
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
    
    public void setNameSignOctaveWithNoteString(Note note, String noteName) {
        note.name = (noteName.charAt(0)+"").toLowerCase();
        note.sign = 'b'==noteName.charAt(1)?"b":note.sign;
        note.sign = '#'==noteName.charAt(1)?"#":note.sign;
        note.octave = parseOctave(noteName);
    }

}
