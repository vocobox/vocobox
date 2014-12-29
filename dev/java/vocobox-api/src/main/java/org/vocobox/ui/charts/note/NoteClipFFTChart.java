package org.vocobox.ui.charts.note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.bluecow.spectro.Clip;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory.Toolkit;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ElapsedTimeTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.ViewportMode;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import org.jzy3d.spectro.primitives.SpectrumSurface;
import org.jzy3d.spectro.trials.SpectrumModelSpectro;
import org.vocobox.model.song.Note;

public class NoteClipFFTChart extends NoteChartAbstract {
    public Clip clip;
    protected AbstractDrawable spectrogram;
    public NoteClipFFTChart(Note note) throws Exception {
        super();
        this.note = note;
        init(note);
    }



    public void init(Note note) throws Exception {
        loadClip(note);
        makeChart(note);
    }


    
    public void loadClip(Note note) throws UnsupportedAudioFileException, IOException {
        clip = Clip.newInstance(note.file);

        SpectrumSurface surface = new SpectrumSurface(new SpectrumModelSpectro(clip), 50);
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);
        spectrogram = surface;
    }
    
    public void makeChart(Note note) {
        if (chart == null) {
            chart = AWTChartComponentFactory.chart(Quality.Intermediate, Toolkit.awt);// toolkit.newt behave differently, "offscreen,100,100"
            chart.getScene().getGraph().add(spectrogram);
            axeLabels(chart);
            make2d(chart);
        }
    }


    protected void axeLabels(Chart chart) {
        IAxeLayout axe = chart.getAxeLayout();
        axe.setXAxeLabel("time");
        axe.setYAxeLabel("freq");
        axe.setZAxeLabel("cos");
    
        axe.setTickLineDisplayed(false);
        axe.setXAxeLabelDisplayed(false);
        axe.setXTickLabelDisplayed(false);
        axe.setYAxeLabelDisplayed(false);
        axe.setYTickLabelDisplayed(true);
    
        axe.setZAxeLabelDisplayed(false);
        axe.setZTickLabelDisplayed(false);
    
        axe.setXTickRenderer(new ElapsedTimeTickRenderer());
        // axe.setYAxeLabel("note");
        // axe.setYTickProvider(new PitchTickProvider());
        // axe.setYTickRenderer(new PitchTickRenderer());
    }

    protected void make2d(Chart chart) {
        chart.getView().setViewPositionMode(ViewPositionMode.TOP);
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
    }

    protected void make3d(Chart chart) {
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
    }


    

/* */
    
    public static List<Chart> getCharts(List<Note> notes) throws Exception {
        List<Chart> charts = new ArrayList<Chart>();
        for (Note n : notes)
            charts.add(new NoteClipFFTChart(n).chart);

        return charts;
    }

    public static Chart[][] getCharts(Note[][] notes) throws Exception {
        Chart[][] charts = new Chart[notes.length][notes[0].length];
        
        for (int i = 0; i < notes.length; i++) {
            for (int j = 0; j < notes[i].length; j++) {
                if(notes[i][j]!=null){
                    charts[i][j] = new NoteClipFFTChart(notes[i][j]).chart;
                    //addChartAt(octave[j], j, i);                    
                }
            }    
        }
        return charts;
    }

}
