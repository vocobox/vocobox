package org.vocobox.model.time;

public class Timer {
    protected long start;
    
    public void start() {
        start = System.nanoTime();
    }

    public double elapsed() {
        return (System.nanoTime() - start) / TimeUtils.TEN_POW_9;
    }

}
