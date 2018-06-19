package space.exploration.spice.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TestFileReading {
    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/roverDB.properties"));

        String line;
        while ((line = (bufferedReader.readLine())) != null) {
            System.out.println(line);
        }
        bufferedReader.close();
    }
}
