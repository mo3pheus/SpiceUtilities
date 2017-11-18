package space.exploration.spice.utilities;

import spice.basic.*;
import spice.basic.CSPICE;
import spice.basic.SpiceErrorException;

public class TimeUtils {

    private static final String LEAP_SECONDS_FILE = "/mslp_1000.data/lsk/naif0012.tls";
    private static final String SCLK_FILE = "/mslsp_1000/data/sclk/msl_76_sclkscet_refit_n4.tsc";

    public static void main(String[] args){
        try {
            CSPICE.furnsh(LEAP_SECONDS_FILE);
            CSPICE.furnsh(SCLK_FILE);
            System.out.println(CSPICE.str2et("09-08-2016 15:32:32"));
        } catch (SpiceErrorException e) {
            e.printStackTrace();
        }
    }
}
