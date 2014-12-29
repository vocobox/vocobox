package org.vocobox.apps.benchmark;

import java.io.File;
import java.io.IOException;

import org.jzy3d.chart.Chart;
import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.apps.benchmark.charts.NotePitchChart;
import org.vocobox.apps.benchmark.charts.PitchEvalChart;
import org.vocobox.io.datasets.HumanVoiceDataset;
import org.vocobox.model.note.Note;
import org.vocobox.model.note.NoteDescriptor;
import org.vocobox.model.note.NoteDescriptors;

public class BenchmarkHumanVoiceNotesPitchEvaluation {
    public static void main(String[] args) throws Exception {
        HumanVoiceDataset voice = HumanVoiceDataset.NOTES;
        
        System.out.println(new File(".").getAbsolutePath());
        
        Note[][] notes = voice.getNoteMatrix();

        Chart[][] charts = evalCharts(notes, 0, 0.5f);
        ui(voice.getNoteHeaders(), charts);
        //ui(voice.getHeaders(), oneNote("C#3", 138.59f, 0.5f));
    }

    
    public static Chart[][] evalCharts(Note[][] notes, int startAt, float semitoneError) throws Exception {
        int octaves = notes.length;
        int semitones = notes[0].length;
        Chart[][] charts = new Chart[(octaves-startAt)*2][semitones];
        
        for (int octave = startAt; octave < octaves; octave++) {
            for (int tone = 0; tone < notes[octave].length; tone++) {
                Note note = notes[octave][tone];
                if(note!=null){
                    charts[octave-startAt][tone] = chartFrequency(note);
                    charts[octave-startAt+octaves][tone] = chartEval(note, semitoneError);
                }
            }    
        }
        return charts;
    }
    
    /**
     * WARN : HACK SHIFT A AND B
     * @param note
     * @return
     * @throws Exception
     */
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
    
    public static Chart[][] oneNote(String name, float expected, float semitoneDistance) throws Exception {
        Note note = HumanVoiceDataset.NOTES.getNote(name);
        Chart[][] charts = new Chart[2][1];
        charts[0][0] = chartFrequency(note);
        charts[0][1] = chartEval(note, semitoneDistance);
        return charts;
    }

}
