package org.vocobox.model.synth;

public class JavaContext {
    public String midiPort;

    public static class JDK6 extends JavaContext {
        {
            midiPort = "Java Sound Synthesizer";
        }
    }

    public static class JDK8 extends JavaContext {
        {
            midiPort = "Gervill";
        }
    }

    public static class MAudio extends JavaContext {
        {
            midiPort = "Port B";
        }
    }

}
