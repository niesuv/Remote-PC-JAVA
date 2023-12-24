package com.util;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ListDir {
    private Path root;
    private BufferedWriter writer;

    public ListDir(String r) throws IOException {
        root = Path.of(r).toAbsolutePath().toRealPath();
    }

    public void listAll(String out) throws IOException {
        writer = new BufferedWriter(new FileWriter(out));
        list(root, "");
        writer.close();
    }

    public void list(Path r, String prefix) throws IOException {
        if (r.equals(root)) {
            writer.write(root.toString());
            writer.newLine();
        }
        Stream<Path> stream = null;
        try {
            stream = Files.list(r);
        }
        catch (AccessDeniedException e) {

        }
        if (stream != null) {
            Path[] ls = stream.toArray(Path[]::new);

            for (int i = 0; i < ls.length; i++) {
                Path p = ls[i];
                if (i == ls.length - 1) {
                    writer.write(prefix);
                    writer.write("└───");
                    writer.write(p.getName(p.getNameCount() - 1).toString());
                    writer.newLine();
                    writer.flush();
                    if (Files.isDirectory(p))
                        list(p, prefix + "   ");
                } else {
                    writer.write(prefix);
                    writer.write("├───");
                    writer.write(p.getName(p.getNameCount() - 1).toString());
                    writer.newLine();
                    writer.flush();
                    if (Files.isDirectory(p))
                        list(p, prefix + "│   ");
                }
            }
            stream.close();

        }


    }

    public static void main(String[] args) {
        try {
            ListDir list = new ListDir("C:\\Users\\huusa\\Desktop");
            list.listAll("out.txt");
        } catch (IOException e) {
            System.out.println("err");
            e.printStackTrace();
        }
    }

}
