package org.vocobox.synth.jsyn.dual;

import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord2d;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.synth.jsyn.dual.AbstractOcclusiveSynth.Period;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;

public class OcclusiveMonitor extends SynthMonitorCharts{
    public OcclusiveMonitor(MonitorSettings settings) {
        super(settings);
    }

    @Override
    public void pitchChangeAtWithInfo(double time, float value, Object info) {

        if (info instanceof Period) {
            Coord2d c = new Coord2d(time, value);
            System.out.println(c.x); // Period period = (Period)info;
            if (Period.OCCLUSIVE.equals(info)) {
                pitchSerie.add(new Coord2d(time, value), Color.GRAY.clone());
            } else if (Period.NOTE.equals(info)) {
                pitchSerie.add(new Coord2d(time, value), settings.pitchColor.clone());
            }
            pitchCursorAnnotation.setValue((float) time);
        }

    }

}
