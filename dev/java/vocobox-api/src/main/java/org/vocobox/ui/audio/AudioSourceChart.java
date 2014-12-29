package org.vocobox.ui.audio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import net.bluecow.spectro.Clip;
import net.bluecow.spectro.ClipPanel;
import net.bluecow.spectro.PlaybackPositionEvent;
import net.bluecow.spectro.PlaybackPositionListener;
import net.bluecow.spectro.PlayerThread;
import net.bluecow.spectro.action.PlayPauseAction;
import net.bluecow.spectro.action.RewindAction;
import net.miginfocom.swing.MigLayout;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory.Toolkit;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.primitives.axes.AxeXLineAnnotation;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ElapsedTimeTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.ViewportMode;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import org.jzy3d.spectro.primitives.SpectrumSurface;
import org.jzy3d.spectro.trials.SpectrumModelSpectro;
import org.vocobox.model.voice.analysis.VocoParseFileAndFFT;
import org.vocobox.ui.Palette;

public class AudioSourceChart {
    // Source file tools
    public ClipPanel spectroPanel;
    public JScrollPane spectroScrollPane;
    public JToolBar sourceFilePlayerToolbar;
    public PlayerThread playerThread;
    public JPanel toolbarPanel;

    protected Toolkit toolkit = Toolkit.awt;
    public Chart spectroChart3d;
    public JPanel spectroChart3dPanel;
    
    public void makeCharts(VocoParseFileAndFFT sourceVocoParse) {
        makePlayerThrows(sourceVocoParse);
        // makeSpectroEditPanelThrows(sourceVocoParse);
        makeSourceFilePlayer();
        makeSpectro3dPanel(sourceVocoParse.getAnalysis());
        
    }

    public void makeSpectroEditPanel(VocoParseFileAndFFT sourceVocoParse) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        spectroPanel = ClipPanel.newInstance(sourceVocoParse.getAnalysis(), playerThread);
        spectroScrollPane = new JScrollPane(spectroPanel);
    }

    public void makePlayer(VocoParseFileAndFFT sourceVocoParse) throws LineUnavailableException {
        playerThread = new PlayerThread(sourceVocoParse.getAnalysis());
        playerThread.start();
    }

    public void makeSpectro3dPanel(final Clip clip) {
        // Create a drawable clip
        int maxFreqId = 50;
        SpectrumSurface surface = new SpectrumSurface(new SpectrumModelSpectro(clip), maxFreqId);
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);

        // Create a chart with time and frequency axes
        spectroChart3d = AWTChartComponentFactory.chart(Quality.Advanced, toolkit);
        spectroChart3d.getScene().getGraph().add(surface);
        
        // audio cursor on player
        final AxeXLineAnnotation ann = new AxeXLineAnnotation();
        final AxeBox axe = (AxeBox)spectroChart3d.getView().getAxe();
        axe.getAnnotations().add(ann);

        
        /*
          frame time samples:1024 frame count:2713 counter:0 playback pos:0
 frame time samples:1024 frame count:2713 counter:768 playback pos:768
 frame time samples:1024 frame count:2713 counter:768 playback pos:768
 frame time samples:1024 frame count:2713 counter:768 playback pos:768
 frame time samples:1024 frame count:2713 counter:768 playback pos:768
 frame time samples:1024 frame count:2713 counter:768 playback pos:768
 frame time samples:1024 frame count:2713 counter:1536 playback pos:1536
 frame time samples:1024 frame count:2713 counter:1536 playback pos:1536
 frame time samples:1024 frame count:2713 counter:1536 playback pos:1536
 frame time samples:1024 frame count:2713 counter:1536 playback pos:1536
 frame time samples:1024 frame count:2713 counter:2304 playback pos:2304
 frame time samples:1024 frame count:2713 counter:2304 playback pos:2304 * 116
 frame time samples:1024 frame count:2713 counter:2304 playback pos:2304
 frame time samples:1024 frame count:2713 counter:3072 playback pos:3072
 
 jusqu'à 2.780.158 / 1024 = 2714 = frame ID!!
 31s
         */
        playerThread.addPlaybackPositionListener(new PlaybackPositionListener() {
            @Override
            public void playbackPositionUpdate(PlaybackPositionEvent e) {
                
                long p = e.getSamplePos();
                //float x = p/44100.0f;
                ann.setValue(p);
                
                
                float frameId = p / playerThread.getClip().getFrameTimeSamples();
                float x = (frameId/clip.getFrameCount()) * axe.getBoxBounds().getXRange().getRange() + axe.getBoxBounds().getXmin();
                //System.out.println(x);
                
                ann.setValue(frameId);
                
                //System.err.println(" frame time samples:" + clip.getFrameTimeSamples() + " frame count:" + clip.getFrameCount() +  " counter:"+p+" playback pos:"+e.getSource().getPlaybackPosition());
                //27130
                
                //if(p>3000)
                //System.exit(0);
                //System.out.println(p);
            }
        });
        
        
        //playerThread.get
        
        // styling
        Palette.apply(spectroChart3d);
        make2d(spectroChart3d);
        IAxeLayout layout = spectroChart3d.getAxeLayout();
        layout.setXTickLabelDisplayed(false);
        layout.setYTickLabelDisplayed(false);
        
        // Embed in jpanel
        spectroPanelAWT();
    }

    // TODO : JZY : not perfect embedding in MIG
    public void spectroPanelMig() {
        spectroChart3dPanel = new JPanel(new MigLayout("", "[600px]", "[600px]"));
        spectroChart3dPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        spectroChart3dPanel.add((java.awt.Component) spectroChart3d.getCanvas(), "cell 0 0, grow");
    }

    public void spectroPanelAWT() {
        spectroChart3dPanel = new JPanel(new BorderLayout());
        Border b = BorderFactory.createLineBorder(Color.black);
        spectroChart3dPanel.setBorder(b);
        spectroChart3dPanel.add((java.awt.Component) spectroChart3d.getCanvas(), BorderLayout.CENTER);
    }

    public JPanel addPanelAt(java.awt.Component panel, int nlin, int ncol) {
        JPanel chartPanel = new JPanel(new BorderLayout());
        Border b = BorderFactory.createLineBorder(Color.black);
        chartPanel.setBorder(b);
        chartPanel.add(panel, BorderLayout.CENTER);
        return chartPanel;
    }

    public void axeLabels(Chart chart) {
        IAxeLayout axe = chart.getAxeLayout();
        axe.setXAxeLabel("time");
        axe.setYAxeLabel("freq");
        axe.setZAxeLabel("cos");

        axe.setXTickRenderer(new ElapsedTimeTickRenderer());
        // axe.setYAxeLabel("note");
        // axe.setYTickProvider(new PitchTickProvider());
        // axe.setYTickRenderer(new PitchTickRenderer());
    }

    public void make2d(Chart chart) {
        chart.getView().setViewPositionMode(ViewPositionMode.TOP);
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        axeLabels(chart);
    }

    public void make3d(Chart chart) {
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        axeLabels(chart);
    }

    public void makeSourceFilePlayer() {
        sourceFilePlayerToolbar = new JToolBar();
        sourceFilePlayerToolbar.add(new PlayPauseAction(playerThread));
        sourceFilePlayerToolbar.add(new RewindAction(playerThread));
        toolbarPanel = new JPanel();
        toolbarPanel.add(sourceFilePlayerToolbar);
    }

    public PlayerThread getClipPlayerThread() {
        return playerThread;
    }

    public void makePlayerThrows(VocoParseFileAndFFT sourceVocoParse) {
        try {
            makePlayer(sourceVocoParse);
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }
    }

    public void makeSpectroEditPanelThrows(VocoParseFileAndFFT sourceVocoParse) {
        try {
            makeSpectroEditPanel(sourceVocoParse);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
