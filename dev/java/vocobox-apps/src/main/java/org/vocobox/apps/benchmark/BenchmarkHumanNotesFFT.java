package org.vocobox.apps.benchmark;

import org.jzy3d.chart.Chart;
import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.io.datasets.HumanVoiceDataset;
import org.vocobox.ui.charts.note.NoteClipFFTChart;

public class BenchmarkHumanNotesFFT {
    public static void main(String[] args) throws Exception {
        Chart[][] charts = NoteClipFFTChart.getCharts(HumanVoiceDataset.NOTES.getNoteMatrix());
        String[] headers = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
        MultiChartPanel monitorPanel = new MultiChartPanel(charts, headers, null, false, 100, 100, false, false);
        monitorPanel.ui();
    }
}
