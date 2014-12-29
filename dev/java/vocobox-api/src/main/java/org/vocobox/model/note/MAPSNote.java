package org.vocobox.model.note;

import java.io.File;

//MAPS
///Users/martin/Datasets/MAPS/MAPS_AkPnBcht_1/AkPnBcht/ISOL/NO
//MAPS_ISOL_NO_P_S0_M30_AkPnBcht.wav

public class MAPSNote extends Note {
    public static MAPSNoteParser PARSER = new MAPSNoteParser();
    // 0 : type : ISOL,
    // 1 : NO type de jeu
    // 2 : loudness P/M/F
    // 3 : Ssustain (true = s1)
    // 4 : midi code
    // 5 : instrument name
    public String type;
    public String playing;
    public String loudness;
    public boolean sustain;
    public int midiCode;
    public String instrument;


    public MAPSNote(File file) {
        this.file = file;
        PARSER.parseNoteFileName(this, file);
    }
/*
    public void parseNoteFileName() {
        Matcher m = pattern.matcher(file.getName());
        if (m.matches()) {
            type = m.group(1);
            playing = m.group(2);
            loudness = m.group(3);
            sustain = "S0".equals(m.group(4))?false:true;
            midiCode = Integer.parseInt(m.group(5));
            instrument = m.group(6);
            
            
            NoteValues nv = NoteTable.inst.getNoteByMidi(midiCode);
            String noteName = nv.name;
            setNameSignOctaveWithNoteString(noteName);
        } else {
            throw new IllegalArgumentException("unrecognize note file name pattern \nfor : " + file.getName() + " \npattern : " + p);
        }
    }



  
*/
    
    @Override
    public String toString() {
        return super.toString() + " | midi:" + midiCode + " type:" + type + " playing:"+ playing + " loudness:"+ loudness + " sustain:"+ sustain + " instrument:" + instrument;
    }

}
