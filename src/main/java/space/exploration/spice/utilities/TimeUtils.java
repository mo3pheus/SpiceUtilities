package space.exploration.spice.utilities;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;

public class TimeUtils {

    File clockFile = null;

    public TimeUtils() {
        clockFile = getFilePath();
    }

    public String getSpacecraftTime(String utcTime) {
        Runtime runtime = Runtime.getRuntime();
        String  output  = "";
        try {
            String[] commands = {"./" + clockFile.getPath(), utcTime};
            Thread.sleep(1000);
            Process           process = runtime.exec(commands);
            InputStream       is      = process.getInputStream();
            InputStreamReader isr     = new InputStreamReader(is);
            BufferedReader    br      = new BufferedReader(isr);
            output = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return output;
    }

    private File getFilePath() {
        String      clockFilePath        = "/mslClock";
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
}
