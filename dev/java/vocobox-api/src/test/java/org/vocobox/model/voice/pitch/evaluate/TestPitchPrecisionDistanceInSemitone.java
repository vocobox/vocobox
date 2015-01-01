package org.vocobox.model.voice.pitch.evaluate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestPitchPrecisionDistanceInSemitone {
    protected PitchPrecisionDistance evaluator;
    
    protected static double PRECISION = 0.01;

    protected static float FRQ_A2_flat = 110.00f;
    protected static float FRQ_A2_bemol = 103.83f;
    protected static float FRQ_A2_sharp = 116.54f;

    protected static float FRQ_A3_flat = 220.00f;

    protected static float FRQ_A4_flat = 440.00f;
    protected static float FRQ_A4_bemol = 415.30f;
    protected static float FRQ_A4_sharp = 466.16f;

    protected static float FRQ_A5_flat = 880.00f;
    
    protected static float FRQ_A6_flat = 1760f;
    protected static float FRQ_A6_bemol = 1661.2f;
    protected static float FRQ_A6_sharp = 1864.7f;
    
    @Before
    public void testEvaluator(){
        evaluator = new PitchPrecisionDistanceInSemitone();
    }
    
    @Test
    public void testBounds(){
        Assert.assertEquals(0, evaluator.distance(FRQ_A4_flat, FRQ_A4_flat), PRECISION); 
        Assert.assertEquals(+1, evaluator.distance(FRQ_A4_flat, FRQ_A4_bemol), PRECISION); 
        Assert.assertEquals(-1, evaluator.distance(FRQ_A4_flat, FRQ_A4_sharp), PRECISION);

        Assert.assertEquals(+1, evaluator.distance(FRQ_A2_flat, FRQ_A2_bemol), PRECISION); 
        Assert.assertEquals(-1, evaluator.distance(FRQ_A2_flat, FRQ_A2_sharp), PRECISION); 

        Assert.assertEquals(-12, evaluator.distance(FRQ_A4_flat, FRQ_A5_flat), PRECISION); 
        Assert.assertEquals(-24, evaluator.distance(FRQ_A4_flat, FRQ_A6_flat), PRECISION); 
        Assert.assertEquals(+12, evaluator.distance(FRQ_A4_flat, FRQ_A3_flat), PRECISION); 
        Assert.assertEquals(+24, evaluator.distance(FRQ_A4_flat, FRQ_A2_flat), PRECISION); 

        Assert.assertEquals(0, evaluator.distance(FRQ_A6_flat, FRQ_A6_flat), PRECISION); 
        Assert.assertEquals(+1, evaluator.distance(FRQ_A6_flat, FRQ_A6_bemol), PRECISION); 
        Assert.assertEquals(-1, evaluator.distance(FRQ_A6_flat, FRQ_A6_sharp), PRECISION);

        Assert.assertEquals(-24, evaluator.distance(FRQ_A4_bemol, FRQ_A6_bemol), PRECISION); 
        Assert.assertEquals(-24, evaluator.distance(FRQ_A4_sharp, FRQ_A6_sharp), PRECISION); 
    }
}
