package org.vocobox.synth.jsyn.dual;


import org.junit.Assert;
import org.junit.Test;
import org.vocobox.model.time.PauseUtils;
import org.vocobox.synth.jsyn.dual.AbstractOcclusiveSynth;
import org.vocobox.synth.jsyn.dual.JsynOcclusiveNoiseSynth;
import org.vocobox.synth.jsyn.dual.AbstractOcclusiveSynth.Period;

public class TestJsynOcclusiveSynth {
    @Test
    public void testTwoPitchCommandAtMoreThanAnOctaveIntervalInLessThan50msSetConfidenceTo_minus1(){
        AbstractOcclusiveSynth synth = new JsynOcclusiveNoiseSynth();
        
        synth.sendFrequency(20);
        PauseUtils.pauseMili(20);
        synth.sendFrequency(41);
        
        Assert.assertEquals("balance is -1", -1,  synth.getBalance(), 0);
        Assert.assertEquals(Period.OCCLUSIVE,  synth.getPeriod());
    }
    
    @Test
    public void testTwoPitchCommandAtMoreThanAnOctaveIntervalInMoreThan50msSetConfidenceTo_1(){
        AbstractOcclusiveSynth synth = new JsynOcclusiveNoiseSynth();
        
        synth.sendFrequency(20);
        PauseUtils.pauseMili(100);
        synth.sendFrequency(41);
        
        Assert.assertEquals("balance is 1", 1,  synth.getBalance(), 0);
        Assert.assertEquals(Period.NOTE,  synth.getPeriod());
    }
    
    @Test
    public void testTwoPitchCommandAtLessThanAnOctaveIntervalInLessThan50msSetConfidenceTo_1(){
        AbstractOcclusiveSynth synth = new JsynOcclusiveNoiseSynth();
        
        synth.sendFrequency(20);
        PauseUtils.pauseMili(30);
        synth.sendFrequency(20);
        
        Assert.assertEquals("balance is 1", 1,  synth.getBalance(), 0);
        Assert.assertEquals(Period.NOTE,  synth.getPeriod());
    }
    
    @Test
    public void canEditTimeWindow(){
        AbstractOcclusiveSynth synth = new JsynOcclusiveNoiseSynth();
        
        synth.sendFrequency(20);
        PauseUtils.pauseMili(20);
        synth.sendFrequency(41);
        Assert.assertEquals("balance is -1", -1,  synth.getBalance(), 0);
        Assert.assertEquals(Period.OCCLUSIVE,  synth.getPeriod());

        // do not forbid to set use note synth (balance=1) if elapsed time bigger than time window
        synth.sendFrequency(20);
        PauseUtils.pauseMili(80);
        synth.sendFrequency(41);
        Assert.assertEquals("balance is 1", 1,  synth.getBalance(), 0);
        Assert.assertEquals(Period.NOTE,  synth.getPeriod());

        // now increase time window and expect to have occlusive synth (balance=-1)
        synth.setConfidenceTimeWindow(100);
        synth.sendFrequency(20);
        PauseUtils.pauseMili(80);
        synth.sendFrequency(41);
        Assert.assertEquals("balance is -1", -1,  synth.getBalance(), 0);
        Assert.assertEquals(Period.OCCLUSIVE,  synth.getPeriod());

        // now reduce : back to balance 1
        synth.setConfidenceTimeWindow(10);
        synth.sendFrequency(20);
        PauseUtils.pauseMili(80);
        synth.sendFrequency(41);
        Assert.assertEquals("balance is 1", 1,  synth.getBalance(), 0);
        Assert.assertEquals(Period.NOTE,  synth.getPeriod());
    }
    

    @Test
    public void canEditOctaveWindow(){
        AbstractOcclusiveSynth synth = new JsynOcclusiveNoiseSynth();
        
        synth.sendFrequency(20);
        PauseUtils.pauseMili(20);
        synth.sendFrequency(41);
        Assert.assertEquals(Period.OCCLUSIVE,  synth.getPeriod());

        // reducing octave distance sensibility to consider 
        // occlusive at less than 1 octave of difference between min/max
        synth.setOctaveOffsetDetectionRatio(0.1);
        synth.sendFrequency(20);
        PauseUtils.pauseMili(20);
        synth.sendFrequency(31);
        Assert.assertEquals(Period.OCCLUSIVE,  synth.getPeriod());
    }

    
    @Test
    public void canMeasureOctaveDiff(){
        AbstractOcclusiveSynth synth = new JsynOcclusiveNoiseSynth();
        Assert.assertTrue(synth.hasMoreThanOctaveRange(20, 41));
        Assert.assertTrue(synth.hasMoreThanOctaveRange(41, 20));
        Assert.assertFalse(synth.hasMoreThanOctaveRange(20, 39));
        Assert.assertFalse(synth.hasMoreThanOctaveRange(39, 20));
    }
}

