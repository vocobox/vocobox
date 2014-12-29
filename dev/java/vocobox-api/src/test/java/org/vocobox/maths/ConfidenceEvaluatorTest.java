package org.vocobox.maths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.vocobox.events.SoundEvent;

public class ConfidenceEvaluatorTest {
    @Test
    public void testConfidence() {
        List<SoundEvent> pitch = new ArrayList<SoundEvent>();
        pitch.add(new SoundEvent(0.00f, 440));
        pitch.add(new SoundEvent(0.01f, 440));
        pitch.add(new SoundEvent(0.02f, 440));

        float timeWindow = 0.050f; // s
        ConfidenceEvaluator evaluator = new ConfidenceEvaluator();
        evaluator.compute(pitch, timeWindow);

        assertConfidence(pitch);
    }

    // pitch unstable : dans la fenêtre d'évaluation il y a une distance
    // d'au moins 1 octove (X*2 hertz) entre min et max
    @Ignore
    @Test
    public void testNoConfidence() {
        List<SoundEvent> pitch = new ArrayList<SoundEvent>();
        pitch.add(new SoundEvent(0.00f, 440));
        pitch.add(new SoundEvent(0.01f, 440));
        pitch.add(new SoundEvent(0.02f, 440));

        float timeWindow = 0.050f; // s
        ConfidenceEvaluator evaluator = new ConfidenceEvaluator();
        evaluator.compute(pitch, timeWindow);

        assertNoConfidence(pitch);
    }

    @Test
    public void testWindowAndEvaluate() {
        List<SoundEvent> pitch = new ArrayList<SoundEvent>();
        pitch.add(new SoundEvent(0.00f, 440));
        pitch.add(new SoundEvent(0.01f, 881)); // <<
        pitch.add(new SoundEvent(0.02f, 440));
        pitch.add(new SoundEvent(0.03f, 440));
        pitch.add(new SoundEvent(0.04f, 440));
        pitch.add(new SoundEvent(0.05f, 440));
        pitch.add(new SoundEvent(0.06f, 440));
        pitch.add(new SoundEvent(0.07f, 440));
        pitch.add(new SoundEvent(0.08f, 440));
        pitch.add(new SoundEvent(0.09f, 440));
        pitch.add(new SoundEvent(0.10f, 440));

        ConfidenceEvaluator evaluator = new ConfidenceEvaluator();

        float timeWindow = 0.05f;

        // stable window
        int winStop = 10;
        int winStart = evaluator.getPreviousWindowStart(pitch, winStop, timeWindow);
        assertEquals(5, winStart);
        assertFalse(evaluator.hasMoreThanOctaveRangeForWindow(pitch, winStart, winStop));

        // unstable window, reaching begining of event list
        winStop = 3;
        winStart = evaluator.getPreviousWindowStart(pitch, winStop, timeWindow);
        assertEquals(0, winStart);
        assertTrue(evaluator.hasMoreThanOctaveRangeForWindow(pitch, winStart, winStop));
    }

    public void assertConfidence(List<SoundEvent> stablePitch) {
        for (SoundEvent pitch : stablePitch) {
            assertEquals(pitch.confidence, 1, 0.000001);
        }
    }

    public void assertNoConfidence(List<SoundEvent> stablePitch) {
        boolean isDifferentThan1 = false;
        for (SoundEvent pitch : stablePitch) {
            if (pitch.confidence != 1) {
                isDifferentThan1 = true;
                break;
            }
        }
        if (!isDifferentThan1)
            Assert.fail("Expected no confidence, all " + stablePitch.size() + " pitches confidences equal to 1");
    }
}
