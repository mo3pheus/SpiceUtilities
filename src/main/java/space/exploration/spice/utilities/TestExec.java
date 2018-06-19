package space.exploration.spice.utilities;

public class TestExec {

    public static void mainTimeUtils(String[] args) throws Exception {
        String    inputTime = "09-30-2016~15:32:32";
        TimeUtils timeUtils = new TimeUtils();
        timeUtils.updateClock(inputTime);

        System.out.println("CalendarTime is :: " + timeUtils.getCalendarTime());
        System.out.println("Clock File is :: " + timeUtils.getClockFile());
        System.out.println("Ephemeris Time is :: " + timeUtils.getEphemerisTime());
        System.out.println("SclkTime is :: " + timeUtils.getSclkTime());
        System.out.println("UTCTime is :: " + timeUtils.getUtcTime());
        System.out.println("Sol is ::" + timeUtils.getSol());
        System.out.println("Applicable timeFrame is :: " + timeUtils.getApplicableTimeFrame());
    }

    public static void mainEphemeris(String[] args) {
        String                  input                   = "515341628";
        EphemerisConversionUtil ephemerisConversionUtil = new EphemerisConversionUtil(args[0]);
        ephemerisConversionUtil.updateClock(input);
        System.out.println(ephemerisConversionUtil.getSclkTime());
    }

    public static void mainClock(String[] args) {
        String    input     = "2016-01-01~00:00:00";
        TimeUtils timeUtils = new TimeUtils(args[0]);
        timeUtils.updateClock(input);
        System.out.println(timeUtils.getSclkTime());
        System.out.println(timeUtils.getEphemerisTime());
    }

    public static void main(String[] args){
        String    input     = "2016-01-01~00:00:00";

    }
}
