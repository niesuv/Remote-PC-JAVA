package com.util;

import java.nio.file.Files;
import java.nio.file.Path;

public class ListDir {
    private static ListDir instance = new ListDir();
    private ListDir() {

    }

    public String list(String _p) {
        Path path = Path.of(_p);
        Files.walkFileTree()

    }
    public static ListDir getInstance() {
        return instance;
    }


}
