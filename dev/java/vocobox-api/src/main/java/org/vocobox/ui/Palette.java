package org.vocobox.ui;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;

public class Palette {
    public static void apply(Chart chart){
        IAxeLayout axe = chart.getAxeLayout();
        axe.setMainColor(Color.WHITE);
        chart.getView().setBackgroundColor(Color.BLACK);
    }
    
    public static void apply(SynthMonitorCharts charts){
        charts.settings.confiColor = Color.BLUE.clone();
        charts.settings.pitchColor = Color.MAGENTA.clone();
        charts.settings.ampliColor = Color.CYAN.clone();
    }
}
