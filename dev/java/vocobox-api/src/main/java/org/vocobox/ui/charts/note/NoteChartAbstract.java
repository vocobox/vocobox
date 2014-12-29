package org.vocobox.ui.charts.note;

import org.jzy3d.chart.Chart;
import org.vocobox.model.song.Note;

public class NoteChartAbstract {
    public Note note;
    protected Chart chart;
    
    public NoteChartAbstract() throws Exception{
    }
    
    public NoteChartAbstract(Note note) {
        this.note = note;
    }
    
    public Chart getChart(){
        return chart;
    }
}