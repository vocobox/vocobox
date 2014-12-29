package org.vocobox.trials.charts;

import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.factories.IChartComponentFactory.Toolkit;
import org.jzy3d.chart2d.Chart2d;
import org.jzy3d.chart2d.Chart2dComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.plot2d.primitives.LineSerie2dSplitted;
import org.jzy3d.plot2d.primitives.Serie2d;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ElapsedTimeTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.View;


/**
 * Showing a pair of 2d charts to represent pitch and amplitude variation of an audio signal
 * 
 * @author Martin Pernollet
 */
public class LineSegmentChart {
    public static float duration = 1.0f;
    
    protected Toolkit toolkit = Toolkit.awt;
    Quality quality = Quality.Intermediate;

    public Chart2d midiNoteChart;
    public LineSerie2dSplitted midiNoteSerie;
    public Color midiNoteColor = Color.YELLOW;


    public static void main(String[] args) throws Exception {
        LineSegmentChart d = new LineSegmentChart();
        d.makeMidiNoteChart(duration);
        
        
        ChartLauncher.openChart(d.midiNoteChart);

        d.midiNoteSerie.add(0.0f, 0.1f, Color.RED);
        d.midiNoteSerie.add(0.1f, 0.2f, Color.BLUE);
        /*d.midiNoteSerie.addOff(0.2f, 0.3f, Color.GREEN);
        
        d.midiNoteSerie.add(0.3f, 0.5f, Color.RED);
        d.midiNoteSerie.add(0.4f, 0.6f, Color.RED);
        d.midiNoteSerie.addOff(0.5f, 0.7f, Color.RED);*/
    }
    

    public void makeMidiNoteChart(float timeMax) {
        midiNoteChart = Chart2dComponentFactory.chart(quality, toolkit);
        midiNoteChartLayout();
        makeMiniNoteSerie();
    }

    public void makeMiniNoteSerie() {
        midiNoteSerie = (LineSerie2dSplitted)midiNoteChart.getSerie("note", Serie2d.Type.LINE_ON_OFF);
        midiNoteSerie.setWidth(20);
        midiNoteSerie.setColor(midiNoteColor);
    }

    public void midiNoteChartLayout() {
 //       Palette.apply(midiNoteChart);
        IAxeLayout axe = midiNoteChart.getAxeLayout();
        axe.setYAxeLabel("time");
        axe.setXAxeLabel("note");
        axe.setXTickRenderer(new ElapsedTimeTickRenderer());
        View view = midiNoteChart.getView();
        view.setBoundManual(new BoundingBox3d(0, 1, 0, 1, -1, 1));
    }
}