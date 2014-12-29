package org.vocobox;

import org.junit.Assert;

public class VocoboxTest {
    /** mocking, otherwise execute really on output midi port*/
    protected boolean mocking = true;
    /** Port name. JDK8 : Gervill | M-Audio Midiman : Port A->D*/
    protected String port = "Gervill";

    // a few test need this precision as we use "low fidelity tables"
    // TODO : SHOULD PERFORM BENCHMARK ON COMPLETE HIGH RES TABLE 
    public static float PRECISION = 0.1f; 


    public void assertEqualsTenMin1(String desc, float expected, float nearest) {
        Assert.assertEquals(desc, expected, nearest, PRECISION);
    }

    public void assertEqualsTenMin5(String desc, float expected, float nearest) {
        Assert.assertEquals(desc, expected, nearest, 0.00001);
    }

    
    public void assertEquals(float expected, float nearest) {
        Assert.assertEquals(expected, nearest, PRECISION);
    }

}
