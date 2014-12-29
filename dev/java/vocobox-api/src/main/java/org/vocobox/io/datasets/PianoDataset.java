package org.vocobox.io.datasets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.vocobox.model.note.MAPSNote;
import org.vocobox.model.note.MAPSNoteParser;
import org.vocobox.utils.EzRgx;

/**
 * MAPS Database helper
 * 
 * 
 * @author Martin Pernollet
 *
 */
// http://www.sengpielaudio.com/calculator-notenames.htm
//MAPS
///Users/martin/Datasets/MAPS/MAPS_AkPnBcht_1/AkPnBcht/ISOL/NO
//MAPS_ISOL_NO_P_S0_M30_AkPnBcht.wav
//0 : type : ISOL, 
//1 : NO type de jeu
//2 : loudness P/M/F
//3 : Ssustain (true = s1)
//4 : midi code
//5 : instrument name
public class PianoDataset extends EzRgx {
    public static PianoDataset MONO = new PianoDataset("/Users/martin/Datasets/MAPS/", "MAPS_AkPnBcht_1/AkPnBcht/ISOL/NO-mono/");
    public static PianoDataset BOESENDORFER = new PianoDataset("/Users/martin/Datasets/MAPS/", "MAPS_AkPnBcht_1/AkPnBcht/ISOL/NO/");
    protected String root;
    protected String dataset;

    public PianoDataset(String root, String dataset) {
        this.root = root;
        this.dataset = dataset;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
    
    
    public String getNoteFilename(String name){
        String file = root + dataset + name;
        return file + ".wav";
    }
    
    public List<MAPSNote> getNotes(){
        List<MAPSNote> notes = new ArrayList<MAPSNote>();
        
        MAPSNoteParser p = new MAPSNoteParser();
        File folder = new File(root + dataset);
        for(File f :folder.listFiles()){
            if(p.isNotePattern(f)){
                notes.add(new MAPSNote(f));
            }
        }
        return notes;
    }
    
}
