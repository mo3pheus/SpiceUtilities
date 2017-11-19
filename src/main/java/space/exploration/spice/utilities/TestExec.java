package space.exploration.spice.utilities;

import spice.basic.Time;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class TestExec {

    public static void main(String[] args) throws Exception {
//        Runtime           runtime = Runtime.getRuntime();
//        Process           process = runtime.exec(args);
//        InputStream       is      = process.getInputStream();
//        InputStreamReader isr     = new InputStreamReader(is);
//        BufferedReader    br      = new BufferedReader(isr);
//        String            line;
//
//        System.out.printf("Output of running %s is:",
//                          Arrays.toString(args));
//
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }

        String inputTime = "09-30-2016~15:32:32";
        System.out.println(TimeUtils.getSpacecraftTime(inputTime));
    }
}
