package org.vocobox.model.time;

public class TimeUtils {
    public static double TEN_POW_9 = 1000000000.0;
    public static double TEN_POW_6 = 1000000.0;

    public static long nowNano(){
        return System.nanoTime();
    }
    
    public static double diffNanoInMs(long nano1, long nano2){
        return (nano2-nano1)/TEN_POW_6;
    }
}
