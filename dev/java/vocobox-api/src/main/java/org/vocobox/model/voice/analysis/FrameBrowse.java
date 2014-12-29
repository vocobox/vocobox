package org.vocobox.model.voice.analysis;

import net.bluecow.spectro.Frame;

public class FrameBrowse {
    public Frame frame;

    public FrameBrowse(Frame frame) {
        this.frame = frame;
        
        int n = frame.getLength();
        
    }
    
    public double frequency(int i){
        int n = frame.getLength();

        double r = i/(double)n;
        return r*maxFreq;//log2(r*maxFreq);
    }
    
    public double log2(double x){
        return Math.log(x)/Math.log(2);
    }
    
    public static double maxFreq = 22050;
}
