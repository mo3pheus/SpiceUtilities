package space.exploration.spice.test;

import junit.framework.TestCase;
import org.junit.Test;
import space.exploration.spice.utilities.EphemerisConversionUtil;

public class EphemerisUtilsTest extends TestCase {
    EphemerisConversionUtil ephemerisConversionUtil = null;

    @Override
    public void setUp(){
        ephemerisConversionUtil = new EphemerisConversionUtil();
    }

    @Test
    public void testEphemerisConversion(){
        ephemerisConversionUtil.updateClock("2016-01-01~00:0:00");
        System.out.println(ephemerisConversionUtil.getSclkTime());
    }
}
