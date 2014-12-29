package org.vocobox.model.time;

public class PauseUtils {
    public static void pauseMili(int mili){
        try {
            Thread.sleep(mili);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
