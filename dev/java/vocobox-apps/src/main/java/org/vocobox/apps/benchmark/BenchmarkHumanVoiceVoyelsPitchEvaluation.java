package org.vocobox.apps.benchmark;

import java.io.IOException;

import org.jzy3d.chart.Chart;
import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.apps.benchmark.charts.NoteMozaic;
import org.vocobox.io.datasets.HumanVoiceDataset;
import org.vocobox.model.synth.MonitorSettings;

public class BenchmarkHumanVoiceVoyelsPitchEvaluation {
    public static void main(String[] args) throws Exception {
        MonitorSettings.OFFSCREEN.applyPalette = false;
        HumanVoiceDataset voice = HumanVoiceDataset.VOYELS;
        Chart[][] charts = NoteMozaic.evalChartsVoyelInstances(NoteMozaic.getVoyelInstancesMatrix(voice.getNotes()));
        ui(voice.getVoyelHeaders(), charts);
    }

    public static void ui(String[] headers, Chart[][] charts) throws IOException {
        MultiChartPanel monitorPanel = new MultiChartPanel(charts, headers, null, false, 100, 100, false, false);
        monitorPanel.ui();
    }
}
