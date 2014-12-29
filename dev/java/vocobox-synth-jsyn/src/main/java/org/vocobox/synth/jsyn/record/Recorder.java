package org.vocobox.synth.jsyn.record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.vocobox.synth.jsyn.JSynVocoSynth;

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.util.WaveRecorder;

public class Recorder {
    private WaveRecorder recorder;
    private JSynVocoSynth synth;

    public Recorder(JSynVocoSynth synth, String file) throws FileNotFoundException {
        wire(synth, file);
    }

    public void wire(JSynVocoSynth synth, String file) throws FileNotFoundException {
        this.synth = synth;
        UnitOutputPort output = synth.getOutput();

        if (output != null) {
            File waveFile = new File(file);
            // Default is stereo, 16 bits.
            recorder = new WaveRecorder(synth.getSynthesizer(), waveFile);
            System.out.println("Writing to WAV file " + waveFile.getAbsolutePath());

            output.connect(0, recorder.getInput(), 0);
            output.connect(0, recorder.getInput(), 1);
        }
        else
            System.err.println(synth.getClass().getSimpleName() + " does not expose its output");
            //Logger.getLogger(Recorder.class).error(synth.getClass().getSimpleName() + " does not expose its output");
    }

    public void recordFor(double duration) throws IOException {
        record();
        sleepDuring(duration);
        stop();
        close();
    }

    public void record() {
        recorder.start();
    }

    public void sleepDuring(double duration) {
        double timeNow = synth.getSynthesizer().getCurrentTime();
        try {
            synth.getSynthesizer().sleepUntil(timeNow + duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        recorder.stop();
    }

    public void close() throws IOException {
        recorder.close();
    }
}
