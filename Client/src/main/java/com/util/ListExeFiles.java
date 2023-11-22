package com.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ListExeFiles {
    private static String[] AllprogramFilesPath = {System.getenv("ProgramFiles"), System.getenv("ProgramFiles(x86)")};

    private static void writeExePathsToFile(String outputPath, String folder) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String programFilesPath : AllprogramFilesPath) {
                if (programFilesPath != null) {
                    writeExePaths(new File(programFilesPath), writer, folder);
                }
            }
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private static void writeExePaths(File directory, BufferedWriter writer, String folder) throws IOException {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    writeExePaths(file, writer, folder);
                } else if (file.isFile() && file.getName().toLowerCase().endsWith(".exe")) {
                    String filePath = file.getAbsolutePath();
                    if (filePath.contains(folder)) {
                        try {
                            writer.write(filePath);
                            writer.newLine();
                        } catch (IOException e) {
                            handleIOException(e);
                        }
                    }
                }
            }
        }
    }

    private static void handleIOException(IOException e) {
        e.printStackTrace();
    }

    public static void main(String[] args) {
        String folderName = "Acer";
        String outputPath = "Output.txt";
        writeExePathsToFile(outputPath, folderName);
    }
}

