package org.vocobox.model.time;

public class Timer {
    protected long start = -1;
    
    public void start() {
        start = System.nanoTime();
    }

    /** time elapsed in second */
    public double elapsed() {
        if(start==-1)
            throw new RuntimeException("timer not started!");
        return (System.nanoTime() - start) / TimeUtils.TEN_POW_9;
    }

}
