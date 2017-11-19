package space.exploration.spice.utilities;

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
}
