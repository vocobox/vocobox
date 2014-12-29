package org.vocobox.apps.benchmark.charts;

import java.util.List;

import org.jzy3d.maths.Coord2d;
import org.vocobox.events.SoundEvent;
import org.vocobox.events.SoundEvent.Type;
import org.vocobox.model.note.Note;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.model.voice.pitch.evaluate.PitchPrecisionLatencyInSemitone;
import org.vocobox.ui.charts.note.NoteChartAbstract;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;
import org.vocobox.voice.pitch.tarsos.VoiceFileRead;

public class PitchEvalChart extends NoteChartAbstract{
    float expectedFrequency;
    double latency;
    
    SynthMonitorCharts monitors;
    
    float semitoneDistance = 12f;

    
    public PitchEvalChart(Note note, float expectedFrequency, float semitoneDistance) throws Exception {
        super(note);
        this.semitoneDistance = semitoneDistance;
        this.expectedFrequency = expectedFrequency;
        monitors = makeSynthMonitor();
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
        
        float latency = latency(events);
        monitors.pitchLatencyCursorAnnotation.setValue(new Coord2d(latency,semitoneDistance));

    }

    public float latency(List<SoundEvent> events) {
        return (float)new PitchPrecisionLatencyInSemitone().latency(events, expectedFrequency, semitoneDistance);
    }

    public SynthMonitorCharts makeSynthMonitor() {
        SynthMonitorCharts monitors = new SynthMonitorCharts(MonitorSettings.OFFSCREEN, false, false){
            @Override
            public void makePitchChartTimeCursor() {
                // no time cursor
            }
        };
        monitors.settings.timeMaxPitchEval = 1.3f;
        chart = monitors.makePitchEvaluationChart(expectedFrequency);
        return monitors;
    }
}
