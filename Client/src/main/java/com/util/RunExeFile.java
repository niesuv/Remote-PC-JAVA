package com.util;
import java.io.IOException;

public class RunExeFile {

    public static int runExecutable(String pathToExe) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(pathToExe);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            return exitCode;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
