package org.vocobox.apps.benchmark.charts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.jzy3d.chart.Chart;
import org.vocobox.io.datasets.HumanVoiceDataset;
import org.vocobox.model.note.MAPSNote;
import org.vocobox.model.note.Note;
import org.vocobox.model.note.NoteDescriptor;
import org.vocobox.model.note.NoteDescriptors;
import org.vocobox.model.note.Voyel;

public class NoteMozaic {
    
    public static Chart[][] oneNote(String name, float expected, float semitoneDistance) throws Exception {
        Note note = HumanVoiceDataset.NOTES.getNote(name);
        Chart[][] charts = new Chart[2][1];
        charts[0][0] = chartFrequency(note);
        charts[0][1] = chartEval(note, semitoneDistance);
        return charts;
    }

    
    public static Chart[][] evalChartsPitch(Note[][] notes, int startAt, float semitoneError) throws Exception {
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

    /* */
    
    public static Chart[][] evalChartsVoyels(List<Note> voyels) throws Exception {
        return evalChartsVoyels(getVoyelMatrix(voyels));
    }
    
    public static Chart[][] evalChartsVoyels(Note[][] voyels) throws Exception {
        Chart[][] charts = new Chart[2][voyels.length];
        for (int tone = 0; tone < voyels.length; tone++) {
            Note note = voyels[tone][0];
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
    
    /* */
    public static List<Chart> charts(List<Note> notes) throws Exception {
        List<Chart> charts = new ArrayList<Chart>();
        for (Note n : notes)
            charts.add(chart(n));

        return charts;
    }

    public static Chart[][] charts(Note[][] notes) throws Exception {
        Chart[][] charts = new Chart[notes.length][notes[0].length];
        
        for (int i = 0; i < notes.length; i++) {
            for (int j = 0; j < notes[i].length; j++) {
                if(notes[i][j]!=null){
                    charts[i][j] = chart(notes[i][j]);
                }
            }    
        }
        return charts;
    }
    
    public static Chart chart(Note n) throws Exception {
        return new NotePitchChart(n).getChart();
    }

    public static Note[][] asNoteMatrix(List<Note> singleNotes) throws UnsupportedAudioFileException, IOException {
        Note[][] notes = new Note[8][12];

        for(Note n: singleNotes){
            //System.out.println(n.order());
            if(n.orderStartAtA()<0)
                System.out.println("excluding " + n);
            else
                notes[n.octave][n.orderStartAtA()] = n;
        }
        return notes;
    }

    public static MAPSNote[][] asMAPSNoteMatrix(List<MAPSNote> singleNotes) throws UnsupportedAudioFileException, IOException {
        MAPSNote[][] notes = new MAPSNote[9][12];

        for(MAPSNote n: singleNotes){
            //System.out.println(n.order());
            if(n.orderStartAtA()<0)
                System.out.println("excluding " + n);
            else
                notes[n.octave][n.orderStartAtA()] = n;
        }
        return notes;
    }

}
