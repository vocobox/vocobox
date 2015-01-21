package org.vocobox.model.synth;

import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapWhiteGreen;
import org.jzy3d.plot2d.primitives.Serie2d.Type;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.vocobox.ui.charts.synth.PitchChartProcessor;
import org.vocobox.ui.charts.synth.PitchChartProcessorDefault;

public class MonitorSettings {
    public static MonitorSettings DEFAULT = new MonitorSettings();
    public static MonitorSettings OFFSCREEN = new OffscreenMonitorSettings();

    public static class OffscreenMonitorSettings extends MonitorSettings{
        public OffscreenMonitorSettings(){
            toolkit = "offscreen,100,100";
        }
    }
    
    public String toolkit = "newt";//Toolkit.newt.toString();
    public Quality quality = Quality.Advanced;
    
    public boolean applyPalette = true;
    public boolean sticky = false;
    public float stickyWindowSize = 5;

    
    public float timeMax = 2;
    public float timeMaxPitchEval = 1;

    public Type pitchSerieType = Type.SCATTER_POINTS;
    public Type ampliSerieType = Type.SCATTER_POINTS;
    public Type confidenceSerieType = Type.SCATTER_POINTS;
    public Type confidenceOnPitchSerieType = Type.SCATTER;
    public Type evaluationSerieType = Type.SCATTER_POINTS;
    
    public float pitchMax = 220;//440;
    public int pitchTickOctaves = 4; //5;
    public float evalMaxErrorInSemitone = 3;
    public float evalPitchLatencyTimeMax = 1.2f;

    // TODO add length of each chart + auto resize policy
    
    public Color pitchColor = Color.MAGENTA;
    public Color ampliColor = Color.CYAN;
    public Color confiColor = Color.GREEN;
    public Color evalColor = Color.RED;
    public Color pitchWithConfidence0 = Color.GRAY;
    public Color midiNoteColor = Color.YELLOW;
    public Color midiPitchColor = Color.MAGENTA;
    public Color midiNoteInferedColor = Color.GRAY.clone().alphaSelf(0.3f);

    public ColorMapper confidenceSerieColormap = new ColorMapper(new ColorMapWhiteGreen(), 0, 1);

    public boolean pitchColorAlphaWithConfidence = true;
    public boolean pitchColorAlphaWithAmplitude = false;
    public float pitchColorAlphaFactor = 1;
    
    public int pitchSerieWidth = 2;
    public int ampliSerieWidth = 2;
    public int confidenceSerieWidth = 3;
    public int evalSerieWidth = 2;
    public int midiSerieWidth = 2;
    public int midiPitchWidth = 2;
    
    /**
     * Coloring with alpha based on amplitude or confidence, depending on {@link MonitorSettings}
     */
    public PitchChartProcessor pitchProcessor = new PitchChartProcessorDefault();
}
