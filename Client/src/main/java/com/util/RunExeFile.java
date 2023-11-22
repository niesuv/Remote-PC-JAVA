package com.util;
import java.io.IOException;

public class RunExeFile {
    public static void runExecutable(String pathToExe) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(pathToExe);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

