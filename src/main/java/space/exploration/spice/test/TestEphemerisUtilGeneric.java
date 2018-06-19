package space.exploration.spice.test;

import space.exploration.spice.utilities.EphemerisConversionUtil;

public class TestEphemerisUtilGeneric {
    public static void main(String[] args) throws Exception {
        EphemerisConversionUtil ephemerisConversionUtil = new EphemerisConversionUtil();
        ephemerisConversionUtil.updateClock("2016-01-01~00:00:00");
        System.out.println(ephemerisConversionUtil.getSclkTime());
    }
}
