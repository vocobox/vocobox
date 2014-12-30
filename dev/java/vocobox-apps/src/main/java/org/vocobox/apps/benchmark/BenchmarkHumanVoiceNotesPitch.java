package org.vocobox.apps.benchmark;

import org.jzy3d.chart.Chart;
import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.apps.benchmark.charts.NoteMozaic;
import org.vocobox.io.datasets.HumanVoiceDataset;
import org.vocobox.model.synth.MonitorSettings;

public class BenchmarkHumanVoiceNotesPitch {
    public static void main(String[] args) throws Exception {
        MonitorSettings.OFFSCREEN.applyPalette = false;
        HumanVoiceDataset voice = HumanVoiceDataset.NOTES;
        Chart[][] charts = NoteMozaic.charts(voice.getNoteMatrix());
        MultiChartPanel monitorPanel = new MultiChartPanel(charts, voice.getNoteHeaders(), null, false, 100, 100, false, false);
        monitorPanel.ui();
    }
}
