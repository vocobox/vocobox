package org.vocobox.apps.benchmark;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jzy3d.chart.Chart;
import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.apps.benchmark.charts.NotePitchChart;
import org.vocobox.apps.benchmark.charts.PitchEvalChart;
import org.vocobox.io.datasets.HumanVoiceDataset;
import org.vocobox.model.note.Note;
import org.vocobox.model.note.NoteDescriptor;
import org.vocobox.model.note.NoteDescriptors;
import org.vocobox.model.note.Voyel;

public class BenchmarkHumanVoiceVoyelsPitchEvaluation {
    public static void main(String[] args) throws Exception {
        HumanVoiceDataset voice = HumanVoiceDataset.VOYELS;
        List<Note> notes = voice.getNotes();
        
        for(Note note: notes){
            System.out.println(note + " " + note.voyel);
        }
        
        Chart[][] charts = evalCharts(getVoyelMatrix(notes));
        ui(voice.getVoyelHeaders(), charts);
    }
    
    public static Chart[][] evalCharts(Note[][] notes) throws Exception {
        Chart[][] charts = new Chart[2][notes.length];
        for (int tone = 0; tone < notes.length; tone++) {
            Note note = notes[tone][0];
            if(note!=null){
                charts[0][tone] = chartFrequency(note);
                charts[1][tone] = chartEval(note, 0.5f);
            }
        }    

        return charts;
    }
    
    public static Note[][] getVoyelMatrix(List<Note> notes){
        Map<Voyel,Integer> counter = getVoyelCount(notes);
        int max = getMaxCounter(counter);
        
        Note[][] matrix = new Note[Voyel.values().length][1];
        for(Note n: notes){
            if(matrix[n.voyel.ordinal()][0] == null){
                matrix[n.voyel.ordinal()][0] = n;
            }
        }
        
        return matrix;
    }

    public static int getMaxCounter(Map<Voyel, Integer> counter) {
        int max = 0;
        for(Map.Entry<Voyel,Integer> e : counter.entrySet()){
            if(e.getValue()>max)
                max = e.getValue();
        }
        return max;
    }

    public static Map<Voyel,Integer> getVoyelCount(List<Note> notes) {
        Map<Voyel,Integer> counter = new HashMap<Voyel,Integer>();
        
        for(Note note: notes){
            Integer i = counter.get(note.voyel);
            if(i==null){
                i = new Integer(1);
            }
            else{
                i = i++;
            }
            counter.put(note.voyel, i);
        }
        return counter;
    }

    public static Chart chartEval(Note note, float semitoneDistance) throws Exception {
        NoteDescriptor descriptor = note.getDescriptor();
        if("a".equals(descriptor.note.name) || "b".equals(descriptor.note.name)){
            descriptor = NoteDescriptors.PIANO.getNoteByKey(descriptor.key-12);
        }
        return new PitchEvalChart(note, (float)descriptor.frequency, semitoneDistance).getChart();
    }

    public static Chart chartFrequency(Note n) throws Exception {
        return new NotePitchChart(n).getChart();
    }

    public static void ui(String[] headers, Chart[][] charts) throws IOException {
        MultiChartPanel monitorPanel = new MultiChartPanel(charts, headers, null, false, 100, 100, false, false);
        monitorPanel.ui();
    }
}
