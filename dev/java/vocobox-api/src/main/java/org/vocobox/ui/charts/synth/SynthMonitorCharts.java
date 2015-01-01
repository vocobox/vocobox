package org.vocobox.ui.charts.synth;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTLogChartComponentFactory;
import org.jzy3d.chart2d.Chart2d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot2d.primitives.Serie2d;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.primitives.axes.AxeCrossAnnotation;
import org.jzy3d.plot3d.primitives.axes.AxeXLineAnnotation;
import org.jzy3d.plot3d.primitives.axes.AxeXRectangleAnnotation;
import org.jzy3d.plot3d.primitives.axes.IAxe;
import org.jzy3d.plot3d.primitives.axes.LogAxeBox;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.primitives.axes.layout.providers.PitchTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.providers.RegularTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.IntegerTickRenderer;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.PitchTickRenderer;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.rendering.view.modes.ViewBoundMode;
import org.jzy3d.plot3d.transform.space.SpaceTransformLog2;
import org.jzy3d.plot3d.transform.space.SpaceTransformer;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.model.synth.VocoSynthMonitor;
import org.vocobox.model.time.Timer;
import org.vocobox.model.voice.pitch.evaluate.PitchPrecisionDistanceInSemitone;
import org.vocobox.ui.Palette;

/**
 * A monitor listening to vocosynth commands change
 * 
 * @author Martin Pernollet
 */
public class SynthMonitorCharts extends Timer implements VocoSynthMonitor {


    public List<Chart> charts = new ArrayList<Chart>();
    public Chart2d pitchChart;
    public Chart2d ampliChart;
    public Chart2d confidenceChart;
    public Chart2d evaluationChart;

    public Serie2d pitchSerie;
    public Serie2d ampliSerie;
    public Serie2d confidenceSerie;
    public Serie2d confidenceOnAmpliSerie;
    public Serie2d confidenceOnPitchSerie;
    public Serie2d evaluationSerie;

    public MonitorSettings settings;

    public AxeXLineAnnotation pitchCursorAnnotation;
    public AxeXLineAnnotation ampliCursorAnnotation;
    public AxeCrossAnnotation pitchLatencyCursorAnnotation;
    // public AxeYLineAnnotation pitchLatencyThresholdCursorAnnotation;

    protected List<Runnable> boundsUpdaters = new ArrayList<Runnable>();
    protected SynthChartFactory factory = new SynthChartFactory(settings);
    protected SynthChartFactory factoryLog = new SynthChartFactory(settings){
        @Override
        public IAxe newAxe(BoundingBox3d box, View view) {
            LogAxeBox axe = new LogAxeBox(box, null); //should support null args, with later definition of transforms
            axe.setScale(new Coord3d(10.0, 1.0, 1.0));
            axe.setView(view);
            return axe;
        }
    };

    // memory of last event info
    protected double currentTime = 0;
    protected float currentConfidence = 1;

    protected float expectedFrequency;


    public PitchPrecisionDistanceInSemitone getPitchPrecisionEvaluator() {
        return new PitchPrecisionDistanceInSemitone();
    }

    /*
     * ###################################################################
     */

    public SynthMonitorCharts() {
        this(MonitorSettings.DEFAULT, true, true);
    }

    public SynthMonitorCharts(MonitorSettings settings) {
        this(settings, true, true);
    }

    public SynthMonitorCharts(MonitorSettings settings, boolean init, boolean startThreads) {
        super();
        this.settings = settings;
        if (init) {
            init(settings, settings.applyPalette);
        }
    }

    
    
    public void init(MonitorSettings settings, boolean applyPalette) {
        if (applyPalette)
            Palette.apply(this); // fix default colors
        makeAllCharts();
        settings.confidenceSerieColormap.getColorMap().setDirection(false);
        runResizingThread();
    }

    public void runResizingThread() {
        if (boundsUpdaters.size() > 0 && settings.sticky)
            runThread();
    }

    /*
     * EVENTS
     * ###################################################################
     */

    @Override
    public void amplitudeChanged(float value) {
        amplitudeChangeAt(elapsed(), value);
    }

    @Override
    public void pitchChanged(float value) {
        pitchChangeAt(elapsed(), value);
    }

    @Override
    public void pitchChanged(float value, Object info) {
        pitchChangeAt(elapsed(), value, info);
    }

    @Override
    public void pitchConfidenceChanged(float value) {
        pitchConfidenceChangedAt(elapsed(), value, 0);
    }

    @Override
    public void onsetOccured(float salience) {
        onsetOccuredAt(elapsed(), salience);
    }

    @Override
    public void offsetOccured() {
    }

    /* Drawing */

    @Override
    public void onsetOccuredAt(double time, float salience) {
        showOnsetAsAxeAnnotation((AxeBox) pitchChart.getView().getAxe(), time);
    }

    @Override
    public void amplitudeChangeAt(double time, float value) {
        currentTime = time;

        Color c = settings.ampliColor.clone();
        c.a = currentConfidence;
        showAmplitude(time, value, c);
    }

    @Override
    public void pitchChangeAt(double time, float value) {
        currentTime = time;

        Color c = settings.pitchColor.clone();
        c.a = currentConfidence;
        showPitch(time, value, c);
        showPitchEvaluation(time, value, getExpectedFrequency(time));
    }

    private float getExpectedFrequency(double time) {
        return expectedFrequency;
    }

    @Override
    public void pitchChangeAt(double time, float value, Object info) {
        if (info == null)
            pitchChangeAt(time, value);
        else {
            pitchChangeAtWithInfo(time, value, info);
            Logger.getLogger(SynthMonitorCharts.class).warn("should re-implement OCCLUSIVE SYNTH");
        }
    }

    public void pitchChangeAtWithInfo(double time, float value, Object info) {
        currentTime = time;
    }

    @Override
    public void pitchConfidenceChangedAt(double time, float confidence, float pitch) {
        currentTime = time;
        currentConfidence = confidence;
        showConfidenceOnPitch(time, confidence, pitch);
        showConfidenceOnAmplitude(time, confidence);
        showConfidenceOnConfidence(time, confidence);
    }

    /* */

    public void showAmplitude(double time, float value, Color c) {
        if (ampliSerie != null) {
            ampliSerie.add(new Coord2d(time, value), c);
        }

        if (ampliCursorAnnotation != null) {
            ampliCursorAnnotation.setValue((float) time);
        }
    }

    public void showPitch(double time, float value, Color c) {
        if (pitchSerie != null) {
            pitchSerie.add(new Coord2d(time, value), c);
        }
        if (pitchCursorAnnotation != null) {
            pitchCursorAnnotation.setValue((float) time);
        }
    }

    public void showPitchEvaluation(double time, float estimatedFrequency, float expectedFrequency) {
        if (evaluationSerie != null) {
            double value = getPitchPrecisionEvaluator().distance(expectedFrequency, estimatedFrequency);
            evaluationSerie.add(time, Math.abs(value), settings.evalColor);
        }
    }

    public void showConfidenceOnConfidence(double time, float confidence) {
        if (confidenceSerie != null)
            confidenceSerie.add(new Coord2d(time, confidence), settings.confidenceSerieColormap.getColor(confidence));
    }

    public void showConfidenceOnAmplitude(double time, float confidence) {
        if (confidenceOnAmpliSerie != null)
            confidenceOnAmpliSerie.add(new Coord2d(time, confidence), settings.pitchWithConfidence0);
    }

    public void showConfidenceOnPitch(double time, float confidence, float pitch) {
        if (confidence == 0 && pitchSerie != null)
            pitchSerie.add(new Coord2d(time, pitch), settings.pitchWithConfidence0);
        if (pitchCursorAnnotation != null)
            pitchCursorAnnotation.setValue((float) time);
        // confOnPitchSerie.add(time, confidence);
    }

    public void showOnsetAsAxeAnnotation(AxeBox axebox, double time) {
        AxeXLineAnnotation onset = new AxeXLineAnnotation();
        onset.setColor(Color.YELLOW);
        onset.setWidth(0.05f);
        onset.setValue((float) time);
        axebox.getAnnotations().add(onset);
    }

    /* */

    @Override
    public void midiNoteOn(int nChannel, int nKey, int nVelocity) {
    }

    @Override
    public void midiNoteOff(int nChannel, int nKey) {
    }

    @Override
    public void midiPitchBend(int nChannel, int value) {
    }

    @Override
    public void midiVolume(int nChannel, int volume) {
    }

    /*
     * CHARTS
     * ####################################################################
     */

    protected Chart2d newChart() {
        return (Chart2d) factory.newChart(settings.quality, settings.toolkit);
    }

    protected Chart2d newChartLog() {
        //
        return (Chart2d) factoryLog.newChart(settings.quality, settings.toolkit);
    }
    
    /* PITCH */

    public void makeAllCharts() {
        makePitchChart();
        makeAmplitudeChart();
        makeConfidenceChart();
    }

    public void makePitchChart() {
        pitchChart = newChart();//Log();
        makePitchChartLayout();
        makePitchSerie();
        makePitchConfidenceSerie();
        addBoundsUpdater(pitchChart);
        charts.add(pitchChart);
        
        // AWTChartComponentFactory
        SpaceTransformer transformYLog2 = new SpaceTransformer();
        transformYLog2.setY(new SpaceTransformLog2());
        // TODO 1:pitchChart.log(transformer);  should set axe, primitives
        // TODO 2:+ future added primitive should have the space transform used
        // TODO 3:space transform at AxeBox level
        // should share the same space transform for all? -> on add drawable via scene graph, add current transform

    
        // chart.normal();
    }

    public void makePitchConfidenceSerie() {
        confidenceOnPitchSerie = pitchChart.getSerie("conf/pitch", settings.confidenceOnPitchSerieType);
        confidenceOnPitchSerie.setWidth(settings.confidenceSerieWidth);
    }

    public void makePitchSerie() {
        pitchSerie = pitchChart.getSerie("frequency", settings.pitchSerieType);
        pitchSerie.setWidth(settings.pitchSerieWidth);
        pitchSerie.setColor(null);
    }

    public void makePitchChartLayout() {
        makePitchChartLayoutBase();
        makePitchAxeLayout();
        makePitchChartTimeCursor();
        makeAutoFitBoundsMode(pitchChart);
    }

    public void makeAutoFitBoundsMode(Chart chart) {
        if (!settings.sticky)
            return;
        final View view = chart.getView();
        view.setBoundManual(new BoundingBox3d(0, settings.timeMax, 0, settings.timeMax, -1, 1));
        view.setBoundMode(ViewBoundMode.AUTO_FIT);
    }

    public void makePitchChartLayoutBase() {
        pitchChart.asTimeChart(settings.timeMax, 0, settings.pitchMax, "Time", "Frequency");
        if (settings.applyPalette)
            Palette.apply(pitchChart);
    }

    public void makePitchAxeLayout() {
        IAxeLayout axe = pitchChart.getAxeLayout();
        axe.setYTickProvider(new PitchTickProvider(settings.pitchTickOctaves));
        axe.setYTickRenderer(new PitchTickRenderer());
    }

    public void makePitchChartTimeCursor() {
        AxeBox axebox = (AxeBox) pitchChart.getView().getAxe();
        pitchCursorAnnotation = new AxeXRectangleAnnotation();
        pitchCursorAnnotation.setColor(Color.GRAY);
        pitchCursorAnnotation.setWidth(0.05f);
        axebox.getAnnotations().add(pitchCursorAnnotation);
    }

    /* PITCH EVALUATION */

    public Chart makePitchEvaluationChart(float expectedFrequency) {
        this.expectedFrequency = expectedFrequency;

        evaluationChart = newChart();
        makePitchEvaluationLayout();//settings.evalPitchLatencyTimeMax);
        makePitchEvaluationSerie();
        makePitchLatencyCursor();

        makeAutoFitBoundsMode(evaluationChart);

        addBoundsUpdater(evaluationChart);
        charts.add(evaluationChart);
        return evaluationChart;
    }

    public void makePitchLatencyCursor() {
        AxeBox axebox = (AxeBox) evaluationChart.getView().getAxe();
        pitchLatencyCursorAnnotation = new AxeCrossAnnotation();
        pitchLatencyCursorAnnotation.setColor(new Color(0, 0.5f, 0));
        pitchLatencyCursorAnnotation.setWidth(1);
        axebox.getAnnotations().add(pitchLatencyCursorAnnotation);
    }

    public void makePitchEvaluationLayout() {
        evaluationChart.asTimeChart(settings.timeMaxPitchEval, 0, settings.evalMaxErrorInSemitone, "Seconds", "Semitone error");
        if (settings.applyPalette)
            Palette.apply(evaluationChart);

        IAxeLayout layout = evaluationChart.getAxeLayout();
        //layout.setXTickLabelDisplayed(false);
        layout.setXTickProvider(new RegularTickProvider((int)settings.timeMax));
        layout.setXTickRenderer(new IntegerTickRenderer());
        layout.setYTickProvider(new RegularTickProvider((int) settings.evalMaxErrorInSemitone+1));
        layout.setYTickRenderer(new IntegerTickRenderer());
        //makeAutoFitBoundsMode(evaluationChart, timeMax);
    }

    public void makePitchEvaluationSerie() {
        evaluationSerie = evaluationChart.getSerie("evaluation", settings.evaluationSerieType);
        evaluationSerie.setWidth(settings.evalSerieWidth);
        evaluationSerie.setColor(null);
    }

    /* AMPLITUDE */

    public void makeAmplitudeChart() {
        ampliChart = newChart();
        makeAmpliChartLayout();
        makeAmplitudeSerie();
        makeAmplitudeConfidenceSerie();
        addBoundsUpdater(ampliChart);
        charts.add(ampliChart);
    }

    public void makeAmplitudeSerie() {
        ampliSerie = ampliChart.getSerie("amplitude", settings.ampliSerieType);
        ampliSerie.setWidth(settings.ampliSerieWidth);
        ampliSerie.setColor(null);
    }

    public void makeAmplitudeConfidenceSerie() {
        confidenceOnAmpliSerie = ampliChart.getSerie("conf/ampli", Serie2d.Type.SCATTER);
        confidenceOnAmpliSerie.setWidth(settings.confidenceSerieWidth);
    }

    public void makeAmpliChartLayout() {
        ampliChart.asTimeChart(settings.timeMax, 0, 1f, "Time", "Amplitude");
        if (settings.applyPalette)
            Palette.apply(ampliChart);
        makeAmpliChartTimeCursor();
        makeAutoFitBoundsMode(ampliChart);
    }

    public void makeAmpliChartTimeCursor() {
        AxeBox axebox = (AxeBox) ampliChart.getView().getAxe();
        ampliCursorAnnotation = new AxeXLineAnnotation();
        ampliCursorAnnotation.setColor(Color.GRAY);
        ampliCursorAnnotation.setWidth(1);
        axebox.getAnnotations().add(ampliCursorAnnotation);
    }

    /* CONFIDENCE */

    public void makeConfidenceChart() {
        confidenceChart = newChart();
        makeConfidenceLayout();
        makeConfidenceSerie();
        addBoundsUpdater(confidenceChart);
        charts.add(confidenceChart);
    }

    public void makeConfidenceLayout() {
        confidenceChart.asTimeChart(settings.timeMax, 0, 1f, "Time", "Confidence");
        if (settings.applyPalette)
            Palette.apply(confidenceChart);
        makeAmpliChartTimeCursor();
        makeAutoFitBoundsMode(confidenceChart);
    }

    public void makeConfidenceSerie() {
        confidenceSerie = confidenceChart.getSerie("confidence", Serie2d.Type.SCATTER_POINTS);
        confidenceSerie.setWidth(settings.confidenceSerieWidth);
        confidenceSerie.setColor(null);
    }

    /* */

    public void clear() {
        pitchSerie.clear();
        ampliSerie.clear();
        if (confidenceSerie != null)
            confidenceSerie.clear();
        confidenceOnPitchSerie.clear();
        confidenceOnAmpliSerie.clear();
    }

    public List<Chart> getCharts() {
        return charts;
    }

    /* ############################################# */

    public void addBoundsUpdater(Chart chart) {
        addBoundsUpdater(chart.getView());
    }

    public void addBoundsUpdater(final View view) {
        if (settings.sticky) {
            boundsUpdaters.add(new Runnable() {
                @Override
                public void run() {

                    // BoundingBox3d b = view.getBounds();

                    view.updateBounds();
                }
            });
        }
    }

    public void runThread() {
        new Thread(getRunnable()).start();
    }

    public Runnable getRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (Runnable boundsUpd : boundsUpdaters) {
                        boundsUpd.run();
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
    }
}
