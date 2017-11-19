package space.exploration.spice.utilities;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;

public class TimeUtils {

    public static String getSpacecraftTime(String filePath, String utcTime) {
        Runtime runtime = Runtime.getRuntime();
        String  output  = "";
        try {
            String[]          commands = {filePath, utcTime};
            Process           process  = runtime.exec(commands);
            InputStream       is       = process.getInputStream();
            InputStreamReader isr      = new InputStreamReader(is);
            BufferedReader    br       = new BufferedReader(isr);
            output = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String getSpacecraftTime(String utcTime) {
        Runtime runtime = Runtime.getRuntime();
        String  output  = "";
        try {
            String[]          commands = {"./" + getFilePath().getPath(), utcTime};
            Process           process  = runtime.exec(commands);
            InputStream       is       = process.getInputStream();
            InputStreamReader isr      = new InputStreamReader(is);
            BufferedReader    br       = new BufferedReader(isr);
            output = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static File getFilePath() {
        String      clockFilePath        = "/mslClock";
        InputStream clockFileInputStream = TimeUtils.class.getResourceAsStream(clockFilePath);
        File        clockFile            = new File("clockFile");
        clockFile.setReadable(true);
        clockFile.setExecutable(true);
        try {
            OutputStream outputStream = new FileOutputStream(clockFile);
            IOUtils.copy(clockFileInputStream, outputStream);
            outputStream.close();
        } catch (IOException io) {
            io.printStackTrace();
        }

        return clockFile;
    }
}
