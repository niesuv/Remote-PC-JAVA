package com.util;

import java.io.*;

public class ListExeFiles {
    private static String[] AllprogramFilesPath = {System.getenv("ProgramFiles"), System.getenv("ProgramFiles(x86)")};
   
    public static String[] cmd = new String[]{"/bin/bash", "-c", "system_profiler SPApplicationsDataType"};
    private static void writeExePathsToFile(String outputPath, String folder) {
        String os = System.getProperty("os.name").toLowerCase();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            if (os.contains("win")) {
                for (String programFilesPath : AllprogramFilesPath) {
                    if (programFilesPath != null) {
                        writeExePaths(new File(programFilesPath), writer, folder);
                    }
                }
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                writeAppPaths(writer);
            }
        } catch (IOException e) { e.printStackTrace();} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeExePaths(File directory, BufferedWriter writer, String folder) throws IOException {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    writeExePaths(file, writer, folder);
                } else if (file.isFile() && (file.getName().toLowerCase().endsWith(".exe"))) {
                    String filePath = file.getAbsolutePath();
                    if (filePath.contains(folder)) {
                        try {
                            writer.write(filePath);
                            writer.newLine();
                        } catch (IOException e) {e.printStackTrace();}
                    }
                }
            }
        }
    }

    private static void writeAppPaths( BufferedWriter writer) throws IOException, InterruptedException {
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("    ") && !line.startsWith("      ")) {
                    writer.write(line);
                }
                if (line.contains("Location")) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            p.waitFor();
        }catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        String folderName = "Acer";
        String outputPath = "Output.txt";
        writeExePathsToFile(outputPath, folderName);
    }
}

