package org.vocobox.events;

import java.util.Arrays;
import java.util.Collection;

import org.jzy3d.maths.Statistics;

public class SoundEventStatistics {
    public static float[] LEVELS = { 0.00f, 0.25f, 0, 50f, 0.75f, 1.00f };

    public float[] values;
    public float[] levels;
    public float[] quantiles;
    public float min;
    public float q25;
    public float q50;
    public float q75;
    public float max;

    public SoundEventStatistics(Collection<SoundEvent> events) {
        compute(events);
    }

    public void compute(Collection<SoundEvent> events) {
        values = SoundEvent.toArray(events);
        levels = LEVELS;
        quantiles = quantile(values, levels);
        min = Statistics.min(values);// quantiles[0];
        q25 = quantiles[1];
        q50 = quantiles[2];
        q75 = quantiles[3];
        max = Statistics.max(values);// quantiles[4];
    }

    public static float[] quantile(float[] values, float[] levels, boolean interpolated) {
        if (values.length == 0)
            return new float[0];

        float[] quantiles = new float[levels.length];
        float[] sorted = new float[values.length];
        System.arraycopy(values, 0, sorted, 0, values.length);
        Arrays.sort(sorted);

        float quantileIdx;
        float quantileIdxCeil;
        float quantileIdxFloor;

        for (int i = 0; i < levels.length; i++) {
            if (levels[i] > 100 || levels[i] < 0)
                throw new IllegalArgumentException("input level " + levels[i] + " is out of bounds [0;100].");

            quantileIdx = (sorted.length - 1) * levels[i] / 100;

            if (quantileIdx == (int) quantileIdx) // exactly find the quantile
                quantiles[i] = sorted[(int) quantileIdx];
            else {
                quantileIdxCeil = (float) Math.ceil(quantileIdx);
                quantileIdxFloor = (float) Math.floor(quantileIdx);

                if (interpolated) { // generate an interpolated quantile
                    quantiles[i] = sorted[(int) quantileIdxFloor] * (quantileIdxCeil - quantileIdx) + sorted[(int) quantileIdxCeil] * (quantileIdx - quantileIdxFloor);
                } else { // return the quantile corresponding to the closest
                         // value
                    if (quantileIdx - quantileIdxFloor < quantileIdxCeil - quantileIdx)
                        quantiles[i] = sorted[(int) quantileIdxFloor];
                    else
                        quantiles[i] = sorted[(int) quantileIdxCeil];
                }
            }
        }
        return quantiles;
    }

    public static float[] quantile(float[] values, float[] levels) {
        return quantile(values, levels, true);
    }

}
