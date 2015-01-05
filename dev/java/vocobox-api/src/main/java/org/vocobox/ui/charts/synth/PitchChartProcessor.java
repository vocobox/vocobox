package org.vocobox.ui.charts.synth;

import org.jzy3d.colors.Color;
import org.vocobox.model.synth.MonitorSettings;

public interface PitchChartProcessor {
    public Color getColor(double time, float value, SynthMonitorCharts charts, MonitorSettings settings);
}
