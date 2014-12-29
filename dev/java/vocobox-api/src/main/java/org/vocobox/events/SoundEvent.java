package org.vocobox.events;

import java.util.Collection;

public class SoundEvent {
    public float timeInSec;
    public float value;
    public float confidence;
    public Type type;
    
    public static enum Type{
        AMPLITUDE, PITCH, ONSET;
    }
    
    public static SoundEvent pitch(float second, float frequency){
        SoundEvent e = new SoundEvent(Type.PITCH, second, frequency);
        return e;
    }

    public static SoundEvent pitch(float frequency){
        return pitch(0, frequency);
    }

    public static SoundEvent amplitude(float second, float amplitude){
        SoundEvent e2 = new SoundEvent(Type.AMPLITUDE, second, amplitude);
        return e2;
    }

    public static SoundEvent amplitude(float amplitude){
        return amplitude(0, amplitude);
    }

    public static SoundEvent onset(float second, float salience){
        SoundEvent e2 = new SoundEvent(Type.ONSET, second, salience);
        return e2;
    }

    
    public SoundEvent(float second, float value) {
        super();
        this.timeInSec = second;
        this.value = value;
        this.confidence = 1;
    }

    public SoundEvent(Type type, float second, float value) {
        super();
        this.type = type;
        this.timeInSec = second;
        this.value = value;
        this.confidence = 1;
    }

    
    public SoundEvent(String second, String value) {
        this(Float.parseFloat(second), Float.parseFloat(value));
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(timeInSec);
        result = prime * result + Float.floatToIntBits(value);
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SoundEvent other = (SoundEvent) obj;
        if (Float.floatToIntBits(timeInSec) != Float.floatToIntBits(other.timeInSec))
            return false;
        if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
            return false;
        return true;
    }
    
    @Override
    public String toString(){
        return type.toString() + "\t" + timeInSec + "\t" + value;
    }
    
    public static float[] toArray(Collection<SoundEvent> events) {
        float[] values = new float[events.size()];
        int k = 0;
        for (SoundEvent c : events) {
            values[k] = c.value;
            k++;
        }
        return values;
    }

}
