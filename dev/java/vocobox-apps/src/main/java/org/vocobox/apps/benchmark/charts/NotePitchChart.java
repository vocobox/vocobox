package org.vocobox.apps.benchmark.charts;

import java.util.List;

import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.primitives.axes.layout.providers.PitchTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.IntegerTickRenderer;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.PitchTickRenderer;
import org.vocobox.events.SoundEvent;
import org.vocobox.events.SoundEvent.Type;
import org.vocobox.model.note.Note;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.model.synth.MonitoredSynth;
import org.vocobox.model.synth.VocoSynth;
import org.vocobox.ui.charts.note.NoteChartAbstract;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;
import org.vocobox.voice.pitch.tarsos.VoiceFilePlay;
import org.vocobox.voice.pitch.tarsos.VoiceFileRead;

public class NotePitchChart extends NoteChartAbstract {

    public NotePitchChart(Note note) throws Exception {
        super(note);
        SynthMonitorCharts monitors = makeSynthMonitor();
        read(note, monitors);
    }

    public List<SoundEvent> read(Note note, SynthMonitorCharts monitors) throws Exception {
        VoiceFileRead voice = new VoiceFileRead();
        List<SoundEvent> events = voice.read(note.file);
        readEventsAndNotify(monitors, events);
        return events;
    }

    public void readEventsAndNotify(SynthMonitorCharts monitors, List<SoundEvent> events) {
        for(SoundEvent event : events){
            if(Type.PITCH.equals(event.type)){
                monitors.pitchChangeAt(event.timeInSec, event.value);                
            }
            else if(Type.AMPLITUDE.equals(event.type)){
                monitors.amplitudeChangeAt(event.timeInSec, event.value);
            }
        }
    }

    public void play(Note note, SynthMonitorCharts monitors) throws Exception {
        VocoSynth synth = new MonitoredSynth(monitors);
        VoiceFilePlay voice = new VoiceFilePlay(synth);
        voice.play(note.file);
    }

    public SynthMonitorCharts makeSynthMonitor() {
        SynthMonitorCharts monitors = new SynthMonitorCharts(MonitorSettings.OFFSCREEN, false, false){
            @Override
            public void makePitchAxeLayout() {
                IAxeLayout axe = pitchChart.getAxeLayout();
                //axe.setXTickLabelDisplayed(false);
                //axe.setXTickProvider(new RegularTickProvider((int)timeMax));
                axe.setXTickRenderer(new IntegerTickRenderer());

                axe.setYTickProvider(new PitchTickProvider(settings.pitchTickOctaves));
                axe.setYTickRenderer(new PitchTickRenderer());
            }
            
            @Override
            public void makePitchChartTimeCursor() {
            }

        };
        monitors.settings.timeMax = 5;
        monitors.settings.pitchMax = 220;
        monitors.settings.pitchTickOctaves = 4;
        monitors.settings.pitchSerieWidth = 2;
        monitors.makePitchChart();
        
        chart = monitors.pitchChart;
        // apply pitch and show 
        //monitors.pitchSerie.add(null, null);
        return monitors;
    }
}
