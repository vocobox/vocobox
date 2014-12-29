package org.vocobox.io;

import org.junit.Assert;
import org.junit.Test;
import org.vocobox.io.EzRgx;

public class TestEzRgx extends EzRgx {
    @Test
    public void testEzRgx() {
        String expected = "(_-" + letter + "{0,2}-){0,1}" + "(" + letter + ")" + "(" + alteration + ")" + "(" + number + ")" + "(-" + numbers + "){0,1}" + "(-" + word + "){0,1}" + ".wav";
        EzRgx rgx = newRgx().optionTwoLetters("_-").note().optionNumbers().optionWord().extWav();
        Assert.assertEquals(expected, rgx.getPattern());
    }
}
