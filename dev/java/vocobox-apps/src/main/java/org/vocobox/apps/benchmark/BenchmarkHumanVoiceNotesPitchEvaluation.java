package org.vocobox.apps.benchmark;

import java.io.IOException;

import org.jzy3d.chart.Chart;
import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.apps.benchmark.charts.NoteMozaic;
import org.vocobox.io.datasets.HumanVoiceDataset;
import org.vocobox.model.synth.MonitorSettings;

public class BenchmarkHumanVoiceNotesPitchEvaluation {
    public static void main(String[] args) throws Exception {
        configureWithArgs(args);
        
        MonitorSettings.OFFSCREEN.applyPalette = false;
        HumanVoiceDataset voice = HumanVoiceDataset.NOTES;
        Chart[][] charts = NoteMozaic.evalChartsPitch(voice.getNoteMatrix(), 0, 0.5f);
        ui(voice.getNoteHeaders(), charts);
    }

    public static void ui(String[] headers, Chart[][] charts) throws IOException {
        MultiChartPanel monitorPanel = new MultiChartPanel(charts, headers, null, false, 100, 100, false, false);
        monitorPanel.ui();
    }
    
    public static void configureWithArgs(String[] args){
        if(args.length>0){
            HumanVoiceDataset.configureDatasetRoot(args[0]);
        }
    }
}
