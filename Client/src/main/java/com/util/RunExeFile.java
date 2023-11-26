package com.util;
import java.io.IOException;

public class RunExeFile {

    public static int runExecutable(String pathToExe) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(pathToExe);
            Process process = processBuilder.start();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
