package org.vocobox.apps.benchmark.charts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.jzy3d.chart.Chart;
import org.vocobox.model.note.MAPSNote;
import org.vocobox.model.song.Note;

public class NoteMozaic {
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
