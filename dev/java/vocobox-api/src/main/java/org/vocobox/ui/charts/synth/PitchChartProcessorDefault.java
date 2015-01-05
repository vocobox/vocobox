package org.vocobox.ui.charts.synth;

import org.jzy3d.colors.Color;
import org.vocobox.model.synth.MonitorSettings;

public class PitchChartProcessorDefault implements PitchChartProcessor {

    @Override
    public Color getColor(double time, float value, SynthMonitorCharts charts, MonitorSettings settings) {
        Color c = newBaseColor(time, value, charts, settings);
        alpha(time, value, charts, settings, c);
        return c;
    }

    /**
     * Apply amplitude value or pitch confidence value to alphe according to settings
     */
    protected void alpha(double time, float value, SynthMonitorCharts charts, MonitorSettings settings, Color c) {
        if (settings.pitchColorAlphaWithConfidence && !settings.pitchColorAlphaWithAmplitude)
            c.a = charts.currentConfidenceReadOnly;
        else if (!settings.pitchColorAlphaWithConfidence && settings.pitchColorAlphaWithAmplitude)
            c.a = charts.currentAmplitudeReadOnly;
        else if (settings.pitchColorAlphaWithConfidence && settings.pitchColorAlphaWithAmplitude)
            c.a = charts.currentAmplitudeReadOnly * charts.currentConfidenceReadOnly;
        c.a = c.a * settings.pitchColorAlphaFactor+0;
    }

    /**
     * Clone a color as defined by settings
     */
    protected Color newBaseColor(double time, float value, SynthMonitorCharts charts, MonitorSettings settings) {
        Color c = settings.pitchColor.clone();
        return c;
    }

}
