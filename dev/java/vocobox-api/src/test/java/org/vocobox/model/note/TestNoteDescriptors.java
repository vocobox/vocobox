package org.vocobox.model.note;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class TestNoteDescriptors {
    float FLOAT_PRECISION = 0.1f;
    
    @Test
    public void testKeyIdToNote(){
        // assert first note
        NoteDescriptor v1 = NoteDescriptors.PIANO.getNoteByKey(1);
        assertNote(v1, "A0", 21, 1, 27.5f);
        
        // assert last note
        NoteDescriptor v88 = NoteDescriptors.PIANO.getNoteByKey(88);
        assertNote(v88, "C8", 108, 88, 4186.0f);

        // assert last note
        assertNote(NoteDescriptors.PIANO.getNoteByMidi(108), "C8", 108, 88, 4186.0f);

    }
    
    @Test
    public void testNoteNameToNote(){
        NoteDescriptor v1 = NoteDescriptors.PIANO.getNoteByName("A0");
        assertNote(v1, "A0", 21, 1, 27.5f);
        assertEquals("a", v1.note.name);
    }
    
    public void assertNote(NoteDescriptor v, String name, int midi, int key, float freq){
        assertNote(v, name,null, midi, key, freq);
    }    
    public void assertNote(NoteDescriptor v, String name, String alternate, int midi, int key, float freq){
        assertEquals(name, v.name);
        assertEquals(midi, v.midi);
        assertEquals(alternate, v.alternativeName);
        assertEquals(key, v.key);
        assertEquals(freq, v.frequency, FLOAT_PRECISION);
    }
}
