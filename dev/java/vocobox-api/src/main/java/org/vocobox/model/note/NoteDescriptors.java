package org.vocobox.model.note;



/**
 * {@link NoteDescriptors.PIANO} is a 88 note array in the range of piano keys (from A0 to C8), 
 * providing note names, frequencies, midi keys, etc.
 * 
 * Lookup methodes allow to easily convert a note to its frequency or midi.
 * 
 * @author Martin Pernollet
 */
public class NoteDescriptors {
    NoteDescriptor[] descritors;
    
    public NoteDescriptors(NoteDescriptor[] descritors) {
        this.descritors = descritors;
    }

    /**
     * @param keyId : piano key in [1;88]
     */
    public NoteDescriptor getNoteByKey(int keyId) {
        return descritors[keyId];
    }
    
    public NoteDescriptor getNoteByMidi(int i) {
        for(NoteDescriptor v: descritors)
            if(v!=null && v.midi==i)
                return v;
        return null;
    }
    
    /**
     * @param note names like A0, C#3, Bb6
     */
    public NoteDescriptor getNoteByName(String name) {
        for(NoteDescriptor v: descritors)
            if(v!=null && v.name.equals(name))
                return v;
        return null;
    }

    /* STATIC */
    
    public static float name2freq(String noteName){
        return PIANO.getNoteByName(noteName).frequency();
    }

    public static int name2midi(String noteName){
        return PIANO.getNoteByName(noteName).midi;
    }

    public static NoteDescriptor[] PIANO_KEYS = new NoteDescriptor[89];
    {
        PIANO_KEYS[88] = new NoteDescriptor("C8", 4186.01, 88);
        PIANO_KEYS[87] = new NoteDescriptor("B7", 3951.07, 87);
        PIANO_KEYS[86] = new NoteDescriptor("A#7", "Bb7", 3729.31, 86);
        PIANO_KEYS[85] = new NoteDescriptor("A7", 3520.00, 85);
        PIANO_KEYS[84] = new NoteDescriptor("G#7", "Ab7", 3322.44, 84);
        PIANO_KEYS[83] = new NoteDescriptor("G7", 3135.96, 83);
        PIANO_KEYS[82] = new NoteDescriptor("F#7", "Gb7", 2959.96, 82);
        PIANO_KEYS[81] = new NoteDescriptor("F7", 2793.83, 81);
        PIANO_KEYS[80] = new NoteDescriptor("E7", 2637.02, 80);
        PIANO_KEYS[79] = new NoteDescriptor("D#7", "Eb7", 2489.02, 79);
        PIANO_KEYS[78] = new NoteDescriptor("D7", 2349.32, 78);
        PIANO_KEYS[77] = new NoteDescriptor("C#7", "Db7", 2217.46, 77);
        PIANO_KEYS[76] = new NoteDescriptor("C7", 2093.00, 76);
        PIANO_KEYS[75] = new NoteDescriptor("B6", 1975.53, 75);
        PIANO_KEYS[74] = new NoteDescriptor("A#6", "Bb6", 1864.66, 74);
        PIANO_KEYS[73] = new NoteDescriptor("A6", 1760.00, 73);
        PIANO_KEYS[72] = new NoteDescriptor("G#6", "Ab6", 1661.22, 72);
        PIANO_KEYS[71] = new NoteDescriptor("G6", 1567.98, 71);
        PIANO_KEYS[70] = new NoteDescriptor("F#6", "Gb6", 1479.98, 70);
        PIANO_KEYS[69] = new NoteDescriptor("F6", 1396.91, 69);
        PIANO_KEYS[68] = new NoteDescriptor("E6", 1318.51, 68);
        PIANO_KEYS[67] = new NoteDescriptor("D#6", "Eb6", 1244.51, 67);
        PIANO_KEYS[66] = new NoteDescriptor("D6", 1174.66, 66);
        PIANO_KEYS[65] = new NoteDescriptor("C#6", "Db6", 1108.73, 65);
        PIANO_KEYS[64] = new NoteDescriptor("C6", 1046.50, 64);
        PIANO_KEYS[63] = new NoteDescriptor("B5", 987.767, 63);
        PIANO_KEYS[62] = new NoteDescriptor("A#5", "Bb5", 932.328, 62);
        PIANO_KEYS[61] = new NoteDescriptor("A5", 880.000, 61);
        PIANO_KEYS[60] = new NoteDescriptor("G#5", "Ab5", 830.609, 60);
        PIANO_KEYS[59] = new NoteDescriptor("G5", 783.991, 59);
        PIANO_KEYS[58] = new NoteDescriptor("F#5", "Gb5", 739.989, 58);
        PIANO_KEYS[57] = new NoteDescriptor("F5", 698.456, 57);
        PIANO_KEYS[56] = new NoteDescriptor("E5", 659.255, 56);
        PIANO_KEYS[55] = new NoteDescriptor("D#5", "Eb5", 622.254, 55);
        PIANO_KEYS[54] = new NoteDescriptor("D5", 587.330, 54);
        PIANO_KEYS[53] = new NoteDescriptor("C#5", "Db5", 554.365, 53);
        PIANO_KEYS[52] = new NoteDescriptor("C5", 523.251, 52);
        PIANO_KEYS[51] = new NoteDescriptor("B4", 493.883, 51);
        PIANO_KEYS[50] = new NoteDescriptor("A#4", "Bb4", 466.164, 50);
        PIANO_KEYS[49] = new NoteDescriptor("A4", 440.000, 49);
        PIANO_KEYS[48] = new NoteDescriptor("G#4", "Ab4", 415.305, 48);
        PIANO_KEYS[47] = new NoteDescriptor("G4", 391.995, 47);
        PIANO_KEYS[46] = new NoteDescriptor("F#4", "Gb4", 369.994, 46);
        PIANO_KEYS[45] = new NoteDescriptor("F4", 349.228, 45);
        PIANO_KEYS[44] = new NoteDescriptor("E4", 329.628, 44);
        PIANO_KEYS[43] = new NoteDescriptor("D#4", "Eb4", 311.127, 43);
        PIANO_KEYS[42] = new NoteDescriptor("D4", 293.665, 42);
        PIANO_KEYS[41] = new NoteDescriptor("C#4", "Db4", 277.183, 41);
        PIANO_KEYS[40] = new NoteDescriptor("C4", 261.626, 40);
        PIANO_KEYS[39] = new NoteDescriptor("B3", 246.942, 39);
        PIANO_KEYS[38] = new NoteDescriptor("A#3", "Bb3", 233.082, 38);
        PIANO_KEYS[37] = new NoteDescriptor("A3", 220.000, 37);
        PIANO_KEYS[36] = new NoteDescriptor("G#3", "Ab3", 207.652, 36);
        PIANO_KEYS[35] = new NoteDescriptor("G3", 195.998, 35);
        PIANO_KEYS[34] = new NoteDescriptor("F#3", "Gb3", 184.997, 34);
        PIANO_KEYS[33] = new NoteDescriptor("F3", 174.614, 33);
        PIANO_KEYS[32] = new NoteDescriptor("E3", 164.814, 32);
        PIANO_KEYS[31] = new NoteDescriptor("D#3", "Eb3", 155.563, 31);
        PIANO_KEYS[30] = new NoteDescriptor("D3", 146.832, 30);
        PIANO_KEYS[29] = new NoteDescriptor("C#3", "Db3", 138.591, 29);
        PIANO_KEYS[28] = new NoteDescriptor("C3", 130.813, 28);
        PIANO_KEYS[27] = new NoteDescriptor("B2", 123.471, 27);
        PIANO_KEYS[26] = new NoteDescriptor("A#2", "Bb2", 116.541, 26);
        PIANO_KEYS[25] = new NoteDescriptor("A2", 110.000, 25);
        PIANO_KEYS[24] = new NoteDescriptor("G#2", "Ab2", 103.826, 24);
        PIANO_KEYS[23] = new NoteDescriptor("G2", 97.9989, 23);
        PIANO_KEYS[22] = new NoteDescriptor("F#2", "Gb2", 92.4986, 22);
        PIANO_KEYS[21] = new NoteDescriptor("F2", 87.3071, 21);
        PIANO_KEYS[20] = new NoteDescriptor("E2", 82.4069, 20);
        PIANO_KEYS[19] = new NoteDescriptor("D#2", "Eb2", 77.7817, 19);
        PIANO_KEYS[18] = new NoteDescriptor("D2", 73.4162, 18);
        PIANO_KEYS[17] = new NoteDescriptor("C#2", "Db2", 69.2957, 17);
        PIANO_KEYS[16] = new NoteDescriptor("C2", 65.4064, 16);
        PIANO_KEYS[15] = new NoteDescriptor("B1", 61.7354, 15);
        PIANO_KEYS[14] = new NoteDescriptor("A#1", "Bb1", 58.2705, 14);
        PIANO_KEYS[13] = new NoteDescriptor("A1", 55.0000, 13);
        PIANO_KEYS[12] = new NoteDescriptor("G#1", "Ab1", 51.9130, 12);
        PIANO_KEYS[11] = new NoteDescriptor("G1", 48.9995, 11);
        PIANO_KEYS[10] = new NoteDescriptor("F#1", "Gb1", 46.2493, 10);
        PIANO_KEYS[9] = new NoteDescriptor("F1", 43.6536, 9);
        PIANO_KEYS[8] = new NoteDescriptor("E1", 41.2035, 8);
        PIANO_KEYS[7] = new NoteDescriptor("D#1", "Eb1", 38.8909, 7);
        PIANO_KEYS[6] = new NoteDescriptor("D1", 36.7081, 6);
        PIANO_KEYS[5] = new NoteDescriptor("C#1", "Db1", 34.6479, 5);
        PIANO_KEYS[4] = new NoteDescriptor("C1", 32.7032, 4);
        PIANO_KEYS[3] = new NoteDescriptor("B0", 30.8677, 3);
        PIANO_KEYS[2] = new NoteDescriptor("A#0", "Bb0", 29.1353, 2);
        PIANO_KEYS[1] = new NoteDescriptor("A0", 27.5000, 1);
    }
    
    public static NoteDescriptors PIANO = new NoteDescriptors(PIANO_KEYS);
}
