package org.vocobox.model.note;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;


public class TestMAPSNote {
    @Test
    public void testMAPSNotePattern(){
        File noteFile = new File("/folder/MAPS_ISOL_NO_P_S0_M30_AkPnBcht.wav");
        File noteFile2 = new File("/folder/MAPS_ISOL_NO_F_S1_M107_AkPnBcht.wav");
        
    
        MAPSNote note = new MAPSNote(noteFile);
        //F#1
        assertEquals("ISOL", note.type);
        assertEquals("NO", note.playing);
        assertEquals("P", note.loudness);
        assertEquals(false, note.sustain);
        assertEquals(30, note.midiCode);
        assertEquals("AkPnBcht", note.instrument);
        assertEquals("f", note.name);
        assertEquals("#", note.sign);
        assertEquals(1, note.octave);


        note = new MAPSNote(noteFile2);
        assertEquals("ISOL", note.type);
        assertEquals("NO", note.playing);
        assertEquals("F", note.loudness);
        assertEquals(true, note.sustain);
        assertEquals(107, note.midiCode);
        assertEquals("AkPnBcht", note.instrument);

        assertEquals("b", note.name);
        assertNull(note.sign);
        assertEquals(7, note.octave);

        MAPSNoteParser p = new MAPSNoteParser();
        assertTrue(p.isNotePattern(noteFile));
        assertTrue(p.isNotePattern(noteFile2));
        assertFalse(p.isNotePattern(new File("/folder/MAPS_ISOL_NO_F_S1_M107_AkPnBcht.txt")));
        assertFalse(p.isNotePattern(new File("/folder/MAPS_ISOL_NO_F_S1_M107_AkPnBcht.mid")));     
    }
}
