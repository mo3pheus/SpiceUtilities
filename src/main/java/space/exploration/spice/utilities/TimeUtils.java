package space.exploration.spice.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class TimeUtils {

    private static final String SCLK_COMPUTATION_FILE = "/SCLK/msl";

    public static String getSpacecraftTime(String utcTime) {
        Runtime runtime = Runtime.getRuntime();
        String  output  = "";
        try {
            URL url = TimeUtils.class.getResource(SCLK_COMPUTATION_FILE);
            String[]          commands = {url.getPath(), utcTime};
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
}
