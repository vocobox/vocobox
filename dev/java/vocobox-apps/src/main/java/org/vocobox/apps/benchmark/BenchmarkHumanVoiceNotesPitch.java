package org.vocobox.apps.benchmark;

import org.jzy3d.chart.Chart;
import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.apps.benchmark.charts.NoteMozaic;
import org.vocobox.io.datasets.HumanVoiceDataset;

public class BenchmarkHumanVoiceNotesPitch {
    public static void main(String[] args) throws Exception {
        String[] headers = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
        Chart[][] charts = NoteMozaic.charts(HumanVoiceDataset.MARTIN.getNoteMatrix());
        MultiChartPanel monitorPanel = new MultiChartPanel(charts, headers, null, false, 100, 100, false, false);
        monitorPanel.ui();
    }
}
