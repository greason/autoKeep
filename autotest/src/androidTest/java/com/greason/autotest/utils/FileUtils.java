package com.greason.autotest.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Greason on 16/09/2017.
 */

public class FileUtils {

    public static String readByBufferedReader(String fileName) {
        String read = "";
        try {
            File file = new File(fileName);
            BufferedReader bufRead;
            bufRead = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufRead.readLine()) != null) {
                read += line;
            }
            bufRead.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return read;
    }

}
