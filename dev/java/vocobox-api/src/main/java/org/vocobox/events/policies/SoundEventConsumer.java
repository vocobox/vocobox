package org.vocobox.events.policies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.vocobox.events.On;
import org.vocobox.events.SoundEvent;
import org.vocobox.events.SoundEventStatistics;
import org.vocobox.events.listeners.SoundEventListener;
import org.vocobox.model.time.PauseUtils;
import org.vocobox.model.time.Timer;

public class SoundEventConsumer extends Timer implements Runnable {
    protected Queue<SoundEvent> eventQueue;
    protected List<SoundEventListener> listeners;
    protected SoundEventStatistics statistics;
    protected int pauseTime = 1;
    protected On listener;
    protected Thread thread;
    protected boolean mustStop;

    public SoundEventConsumer(Collection<SoundEvent> events) {
        this.eventQueue = new ConcurrentLinkedQueue<SoundEvent>(events);
        this.listeners = new ArrayList<SoundEventListener>();
        this.thread = new Thread(this); // Use thread pool

    }

    public void computeStatistics(Collection<SoundEvent> events) {
        this.statistics = new SoundEventStatistics(events);
    }
    
    public void runnerStart() {
        mustStop = false;
        runner().start();
    }

    public void runnerStop() {
        mustStop = true;
    }


    @Override
    public void run() {
        start();
        mustStop = false;
        while (!eventQueue.isEmpty()) {
            if(mustStop)
                break;
            SoundEvent event = eventQueue.remove();
            SoundEvent latest = waitFor(event);
            trigger(latest);
        }
        if(listener!=null)
            listener.finish();
    }

    public Thread runner() {
        return thread;
    }

    public Thread runner(On listener) {
        this.listener = listener;
        return new Thread(this);
    }

    
    private void trigger(SoundEvent event) {
        for (SoundEventListener listener : listeners) {
            listener.event(event);
        }
        //System.out.println(event.type + " " + event.value);
    }

    /**
     * Wait for the given event to be likely to occur given elapsed time.
     * 
     * When eligible for trigger, check if following events should be triggered
     * according to current time, and actually trigger latest event.
     * 
     * @return Either input ControlEvent if immediately eligible, or the latest
     *         eligible in the list.
     */
    // TODO : sleep in nanosecond or adapt to frame rate
    // wait
    // should use join etc?
    public SoundEvent waitFor(SoundEvent event) {
        double elapsed = elapsed();
        boolean hasNext = false;
        SoundEvent next = null;
        while (event.timeInSec > elapsed) {
/*            next = eventQueue.iterator().next();
            while (next.second < elapsed) {
                next = eventQueue.iterator().next();
                hasNext = true;
            }*/
            
            PauseUtils.pauseMili(pauseTime);
            elapsed = elapsed();
        }
        if (hasNext)
            return next;
        else
            return event;
    }

    public void addListener(SoundEventListener listener) {
        listeners.add(listener);
    }

    public List<SoundEventListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<SoundEventListener> listeners) {
        this.listeners = listeners;
    }

    public SoundEventStatistics getStatistics() {
        return statistics;
    }

}
