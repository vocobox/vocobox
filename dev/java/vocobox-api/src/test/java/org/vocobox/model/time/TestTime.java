package org.vocobox.model.time;


import org.junit.Assert;
import org.junit.Test;

public class TestTime {
    @Test
    public void testTimeDiffNanoInMs(){
        // 1ms (10^-3) = 100.000nano
        Assert.assertEquals(1, TimeUtils.diffNanoInMs(0, 1000000), 0);
    }
}
