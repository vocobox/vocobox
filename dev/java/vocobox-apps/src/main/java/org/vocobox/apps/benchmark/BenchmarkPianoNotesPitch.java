package org.vocobox.apps.benchmark;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jzy3d.chart.Chart;
import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.apps.benchmark.charts.NoteMozaic;
import org.vocobox.io.datasets.PianoDataset;
import org.vocobox.model.note.MAPSNote;

public class BenchmarkPianoNotesPitch {
    public static void main(String[] args) throws Exception {
        List<MAPSNote> notes = PianoDataset.BOESENDORFER.getNotes();
        /*for(MAPSNote note: notes){
            System.out.println(note);
        }*/
        
        MAPSNote[][] matrix = NoteMozaic.asMAPSNoteMatrix(notes);
        Chart[][] charts = NoteMozaic.charts(matrix);
        String[] headers = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
        MultiChartPanel monitorPanel = new MultiChartPanel(charts, headers, null, false, 100, 100, false, false);
        JScrollPane pane = new JScrollPane(monitorPanel);
        JPanel parent = new JPanel();
        parent.add(pane);
        MultiChartPanel.frame(parent);
    }
}
