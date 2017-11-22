package space.exploration.spice.utilities;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class TimeUtils {

    File clockFile = null;
    private double ephemerisTime = 0.0d;
    private String calendarTime  = "";
    private String sclkTime      = "";
    private String utcTime       = "";

    public enum SCHEMA {
        SCLK_STR(0), EPHEMERIS_TIME(1), CALENDAR_TIME(2);
        int value;

        SCHEMA(int val) {
            value = val;
        }

        public String getSchema() {
            return "sclkString,ephemerisTime,calendarTime";
        }
    }

    private File getFilePath() {
        //String      clockFilePath        = "/mslClock";
        String      clockFilePath        = "/SCLK/msl";
        InputStream clockFileInputStream = TimeUtils.class.getResourceAsStream(clockFilePath);
        File        clockFile            = new File("clockFile");
        clockFile.setReadable(true);
        clockFile.setExecutable(true);
        try {
            Thread.sleep(1000);
            OutputStream outputStream = new FileOutputStream(clockFile);
            IOUtils.copy(clockFileInputStream, outputStream);
            outputStream.close();
        } catch (IOException io) {
            io.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return clockFile;
    }

    public TimeUtils() {
        clockFile = getFilePath();
    }

    public void updateClock(String utcTime) {
        this.utcTime = utcTime;
        getSpacecraftTimePacket();
    }

    private void getSpacecraftTimePacket() {
        Runtime runtime = Runtime.getRuntime();
        String  output  = "";
        try {
            String[]          commands = {"./" + clockFile.getPath(), utcTime};
            Process           process  = runtime.exec(commands);
            InputStream       is       = process.getInputStream();
            InputStreamReader isr      = new InputStreamReader(is);
            BufferedReader    br       = new BufferedReader(isr);
            output = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] outputParts = output.split(",");

        sclkTime = outputParts[SCHEMA.SCLK_STR.value];
        ephemerisTime = Double.parseDouble(outputParts[SCHEMA.EPHEMERIS_TIME.value]);
        calendarTime = outputParts[SCHEMA.CALENDAR_TIME.value];
    }

    public File getClockFile() {
        return clockFile;
    }

    public double getEphemerisTime() {
        return ephemerisTime;
    }

    public String getCalendarTime() {
        return calendarTime;
    }

    public String getSclkTime() {
        return sclkTime;
    }

    public String getUtcTime() {
        return utcTime;
    }

    public String getApplicableTimeFrame() {
        return "APPLICABLE_START_TIME=2000-001T11:58:55.816, APPLICABLE_STOP_TIME=2017-360T19:17:40.149";
    }
}
