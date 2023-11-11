package com.util;

import java.io.IOException;

public class ListApp {

    public static void main(String[] args) {
        try {
            String[] cmd = new String[]{"powershell","-command",
            "\"Get-ItemProperty HKLM:\\Software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\* | Select-Object DisplayName, DisplayVersion, Publisher, InstallDate | Format-Table –AutoSize\""};
            Process p = Runtime.getRuntime().exec(cmd);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
