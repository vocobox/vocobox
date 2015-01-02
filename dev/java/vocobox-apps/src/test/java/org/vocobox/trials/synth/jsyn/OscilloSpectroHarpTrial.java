package org.vocobox.trials.synth.jsyn;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.bluecow.spectro.Clip;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ElapsedTimeTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.ViewportMode;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import org.jzy3d.spectro.primitives.SpectrumSurface;
import org.jzy3d.spectro.trials.SpectrumModelSpectro;
import org.vocobox.synth.jsyn.harp.JsynOscilloSpectroHarpSynth;

/**
 * Use a FFT to configure multiple oscillator having frequencies setup at each 
 * frequency band of the FFT spectrum.
 * 
 * In this demonstration, the "harp" is based on 93 oscillators. Their amplitude is defined 
 * by the current FFT spectrum while reading an input file.
 * 
 * @author Martin Pernollet
 */
public class OscilloSpectroHarpTrial extends AbstractAnalysis {
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new OscilloSpectroHarpTrial());
    }

    @Override
    public void init() throws UnsupportedAudioFileException, IOException {
        File file = new File("data/sound/piano.wav");
        Clip clip = Clip.newInstance(file);
        SpectrumModelSpectro spectrum = new SpectrumModelSpectro(clip);
        
        // --------------------
        JsynOscilloSpectroHarpSynth synth = new JsynOscilloSpectroHarpSynth(spectrum, 4000);

        // --------------------
        // Create a drawable clip
        int maxFreqId = 50;
        SpectrumSurface surface = new SpectrumSurface(spectrum, maxFreqId);
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);

        // Create a chart with time and frequency axes
        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
        chart.getScene().getGraph().add(surface);
        make3d(chart);
        
        synth.play();
    }

    public void axeLabels(Chart chart) {
        IAxeLayout axe = chart.getAxeLayout();
        axe.setXAxeLabel("time");
        axe.setYAxeLabel("freq");
        axe.setZAxeLabel("");

        axe.setXTickRenderer(new ElapsedTimeTickRenderer());
    }

    public void make2d(Chart chart) {
        chart.getView().setViewPositionMode(ViewPositionMode.TOP);
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        axeLabels(chart);
    }

    public void make3d(Chart chart) {
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        // chart.getScene().getGraph().setStrategy(new
        // BarycentreOrderingStrategy()); // not resolving issue3
        axeLabels(chart);
    }
}