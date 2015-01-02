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

    public static class Port_A extends JavaContext {
        {
            midiPort = "Port A";
        }
    }

    public static class Port_B extends JavaContext {
        {
            midiPort = "Port B";
        }
    }

    public static class Port_C extends JavaContext {
        {
            midiPort = "Port C";
        }
    }

    public static class Port_D extends JavaContext {
        {
            midiPort = "Port D";
        }
    }

}
