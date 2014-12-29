package org.vocobox.events.listeners;

import org.vocobox.events.SoundEvent;

public class SoundEventListenerLogger implements SoundEventListener{

    @Override
    public void event(SoundEvent event) {
        System.out.println(event.timeInSec + "\t" + event.value);
    }
}
