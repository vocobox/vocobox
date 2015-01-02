package org.vocobox.trials.charts;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.spectro.primitives.SpectrumSurface;
import org.jzy3d.spectro.trials.SpectrumModelSpectro;
import org.vocobox.model.voice.analysis.VocoParseFileAndFFT;

/** Use spectro-edit to retrieve FFT of a song by frames */
public class VocoParseFFT3dTrial {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        VocoParseFileAndFFT voice = new VocoParseFileAndFFT(new File("data/sound/piano.wav"));
        Chart chart = AWTChartComponentFactory.chart();
        SpectrumSurface d = new SpectrumSurface(new SpectrumModelSpectro(voice.getAnalysis()));
        chart.addDrawable(d);
        ChartLauncher.openChart(chart);
    }
}
