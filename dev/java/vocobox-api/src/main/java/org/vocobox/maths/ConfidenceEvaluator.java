package org.vocobox.maths;

import java.util.List;

import org.vocobox.events.SoundEvent;

public class ConfidenceEvaluator {
    // pitch unstable : dans la fenêtre d'évaluation il y a une distance
    // d'au moins 1 octove (X*2 hertz) entre min et max

    public void compute(List<SoundEvent> pitch, double timeWindow) {
        // si suffisament de

        // pour chaque event : reconstruire les X premières secondes
        for (int i = 0; i < pitch.size(); i++) {
            int winStop = i;
            int winStart = getPreviousWindowStart(pitch, i, timeWindow);
            if(hasMoreThanOctaveRangeForWindow(pitch, winStart, winStop)){
                pitch.get(i).confidence = 0;
            }
        }
    }

    // ID range for time window
    protected int getPreviousWindowStart(List<SoundEvent> pitch, int pitchEvent, double timeWindow) {
        if(pitchEvent==0)
            return 0;
        SoundEvent currentPitch = pitch.get(pitchEvent);
        pitchEvent--;

        while (pitchEvent >= 0) {
            SoundEvent previousPitch = pitch.get(pitchEvent);
            if (currentPitch.timeInSec - previousPitch.timeInSec < timeWindow) {
                if (pitchEvent == 0)
                    return 0;
                pitchEvent--;
            } else
                return pitchEvent;
        }
        return pitchEvent;
    }

    protected boolean hasMoreThanOctaveRangeForWindow(List<SoundEvent> pitch, int winStart, int winStop) {
        float min = Float.POSITIVE_INFINITY;
        float max = Float.NEGATIVE_INFINITY;

        for (int i = winStart; i <= winStop; i++) {
            SoundEvent p = pitch.get(i);
            if (p.value < min) {
                min = p.value;
            }
            if (p.value > max) {
                max = p.value;
            }
        }
        return max > min * 2;
    }
}
