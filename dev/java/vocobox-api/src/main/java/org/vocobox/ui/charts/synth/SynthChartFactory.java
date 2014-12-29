package org.vocobox.ui.charts.synth;

import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.chart2d.Chart2dComponentFactory;
import org.vocobox.model.synth.MonitorSettings;

public class SynthChartFactory extends Chart2dComponentFactory{
    MonitorSettings settings;

    public SynthChartFactory(MonitorSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public IChartComponentFactory getFactory() {
        return this;
    }
}
