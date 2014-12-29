package org.vocobox.model.song;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.vocobox.model.song.Note;

public class TestNote {
    @Test
    public void testRegexp() {
        Note note_aD2 = new Note("folder/a#2.wav");
        assertEquals("a", note_aD2.name);
        assertEquals("#", note_aD2.sign);
        assertEquals(2, note_aD2.octave);
        assertEquals(1, note_aD2.instance);
        assertEquals(1, note_aD2.orderStartAtA());
    }

    @Test
    public void testOrderStartAtA() {
        assertEquals(0, new Note("a2.wav").orderStartAtA());
        assertEquals(1, new Note("a#2.wav").orderStartAtA());
        assertEquals(2, new Note("b2.wav").orderStartAtA());
        assertEquals(3, new Note("c2.wav").orderStartAtA());
        assertEquals(4, new Note("c#2.wav").orderStartAtA());
        assertEquals(5, new Note("d2.wav").orderStartAtA());
        assertEquals(6, new Note("d#2.wav").orderStartAtA());
        assertEquals(7, new Note("e2.wav").orderStartAtA());
        assertEquals(8, new Note("f2.wav").orderStartAtA());
        assertEquals(9, new Note("f#2.wav").orderStartAtA());
        assertEquals(10, new Note("g2.wav").orderStartAtA());
        assertEquals(11, new Note("g#2.wav").orderStartAtA());

    }

    @Test
    public void testOrderStartAtC() {
        assertEquals(0, new Note("c2.wav").orderStartAtC());
        assertEquals(1, new Note("c#2.wav").orderStartAtC());
        assertEquals(2, new Note("d2.wav").orderStartAtC());
        assertEquals(3, new Note("d#2.wav").orderStartAtC());
        assertEquals(4, new Note("e2.wav").orderStartAtC());
        assertEquals(5, new Note("f2.wav").orderStartAtC());
        assertEquals(6, new Note("f#2.wav").orderStartAtC());
        assertEquals(7, new Note("g2.wav").orderStartAtC());
        assertEquals(8, new Note("g#2.wav").orderStartAtC());
        assertEquals(9, new Note("a2.wav").orderStartAtC());
        assertEquals(10, new Note("a#2.wav").orderStartAtC());
        assertEquals(11, new Note("b2.wav").orderStartAtC());

    }

    @Test
    public void testMidi() {
        assertEquals(21, new Note("a0.wav").getDescriptor().midi);
        assertEquals(45, new Note("a2.wav").getDescriptor().midi);
        assertEquals(48, new Note("c3.wav").getDescriptor().midi);
        assertEquals(45, new Note("a2.wav").getDescriptor().midi);
        assertEquals(108, new Note("c8.wav").getDescriptor().midi);
    }
}
