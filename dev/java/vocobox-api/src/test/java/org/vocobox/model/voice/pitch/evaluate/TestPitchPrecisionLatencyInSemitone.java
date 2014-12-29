package org.vocobox.model.voice.pitch.evaluate;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.vocobox.events.SoundEvent;
import org.vocobox.model.note.NoteDescriptors;

public class TestPitchPrecisionLatencyInSemitone {
    PitchPrecisionLatency evaluator;
    
    public static double PRECISION = 0.01;
    
    @Before
    public void setUp()  {
        evaluator = new PitchPrecisionLatencyInSemitone();
    }

    @Test
    public void testLatency(){
        List<SoundEvent> commands = new ArrayList<SoundEvent>();
        double latency;
        int semitonDistance = 1;
        
        // test distance function
        Assert.assertEquals(2, evaluator.distance(SoundEvent.pitch(freq("D3")), freq("C3")), PRECISION);
        
        // test latency function
        commands.clear();
        commands.add(SoundEvent.pitch(0.0f, freq("D3")));
        commands.add(SoundEvent.pitch(0.5f, freq("C#3"))); // <<
        commands.add(SoundEvent.pitch(1.0f, freq("C3")));
        latency = evaluator.latency(commands, freq("C3"), semitonDistance);
    
        Assert.assertEquals(0.5, latency, PRECISION);

        // another latency test
        semitonDistance = 2;
        commands.clear();
        commands.add(SoundEvent.pitch(0.0f, freq("D5")));
        commands.add(SoundEvent.pitch(0.1f, freq("D6")));
        commands.add(SoundEvent.pitch(0.2f, freq("D5")));
        commands.add(SoundEvent.pitch(0.3f, freq("B2"))); //<<
        commands.add(SoundEvent.pitch(0.4f, freq("D3")));
        commands.add(SoundEvent.pitch(0.5f, freq("B2")));
        commands.add(SoundEvent.pitch(0.6f, freq("C3")));
        commands.add(SoundEvent.pitch(0.7f, freq("B2")));
        latency = evaluator.latency(commands, freq("B2"), semitonDistance);

        Assert.assertEquals(0.3, latency, PRECISION);
        
        // never converge latency
        semitonDistance = 2;
        commands.clear();
        commands.add(SoundEvent.pitch(0.0f, freq("D5")));
        commands.add(SoundEvent.pitch(0.1f, freq("D6")));
        latency = evaluator.latency(commands, freq("B2"), semitonDistance);

        Assert.assertEquals(PitchPrecisionLatency.NO_CONVERGE, latency, PRECISION);

        // already converge latency
        semitonDistance = 1;
        commands.clear();
        commands.add(SoundEvent.pitch(0.0f, freq("B2")+0.002f));
        commands.add(SoundEvent.pitch(0.1f, freq("B2")+0.001f));
        latency = evaluator.latency(commands, freq("B2"), semitonDistance);

        Assert.assertEquals(0, latency, PRECISION);

    }

    public float freq(String note) {
        return NoteDescriptors.name2freq(note);
    }
}
