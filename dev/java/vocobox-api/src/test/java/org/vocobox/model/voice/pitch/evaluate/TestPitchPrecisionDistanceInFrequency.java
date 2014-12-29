package org.vocobox.model.voice.pitch.evaluate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.vocobox.model.voice.pitch.evaluate.PitchPrecisionDistance;
import org.vocobox.model.voice.pitch.evaluate.PitchPrecisionDistanceInFrequency;


public class TestPitchPrecisionDistanceInFrequency {
    protected PitchPrecisionDistance evaluator;
    
    protected static double PRECISION = 0.01;
    
    @Before
    public void testEvaluator(){
        evaluator = new PitchPrecisionDistanceInFrequency();
    }
    
    @Test
    public void testDistance(){
        Assert.assertEquals(1.34, evaluator.distance(440.00f, 441.34f), PRECISION);        
        Assert.assertEquals(1.34, evaluator.distance(220.00f, 221.34f), PRECISION);        
        Assert.assertEquals(1.34, evaluator.distance(110.00f, 111.34f), PRECISION); 
        Assert.assertEquals(1.34, evaluator.distance(055.00f, 056.34f), PRECISION); 
        Assert.assertEquals(1.34, evaluator.distance(022.50f, 023.84f), PRECISION); 
        
        Assert.assertEquals(0.00, evaluator.distance(220.00f, 220.00f), PRECISION);        
        Assert.assertEquals(0.01, evaluator.distance(220.00f, 220.01f), PRECISION);        
    }
}
