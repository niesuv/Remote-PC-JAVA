package com.util;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ProcessPC {
    String os;
    Runtime runtime;

    private static ProcessPC instance = new ProcessPC();
    public ProcessPC(){
        try {
            this.os = System.getProperty("os.name").toLowerCase();
            this.runtime = Runtime.getRuntime();
        }catch (Exception e){ System.out.println(e.toString());}
    }
    public static ProcessPC getInstance() {
        return instance;
    }

    public String ProcessList(){
        try{
            Process process = null;
            if (this.os.contains("win")) { process = runtime.exec("tasklist");}
            else if (this.os.contains("mac")||this.os.contains("nux") || this.os.contains("nix")) { process = runtime.exec("ps aux");}
            if (process!=null) {
                String filename ="ProcessList " + ZonedDateTime.now().format(DateTimeFormatter
                        .ofPattern("dd-MM-yyyy HH-mm")) +".txt";
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.close();
                reader.close();
                return filename;
            }else{ System.out.println("Unsupported operating system");}
        }catch (Exception e){ System.out.println(e.toString()); }
        return null;
    }


    public void StopProcess(String appname){
        try{
            if(this.os.contains("win")) {
                if(appname.contains("exe")) { this.runtime.exec("taskkill /F /IM " + appname);}
                else{ this.runtime.exec("taskkill /F /IM " + appname + ".exe");}
                System.out.println("Kill "+ appname+" successfully");
            } else if (this.os.contains("mac")||this.os.contains("nix")||this.os.contains("nux")) {
                this.runtime.exec("pkill -f " + appname);
                System.out.println("Kill " + appname + " successfully");
            } else { System.out.println("Unsupported operating system");}
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    public void StopProcess(int processid){
        try{
            if(this.os.contains("win")) {
                this.runtime.exec("taskkill /F /PID "+processid);
                System.out.println("Kill "+ processid+" successfully");
            } else if (this.os.contains("mac")||this.os.contains("nix")||this.os.contains("nux")) {
                this.runtime.exec("kill " + processid);
                System.out.println("Kill " + processid + " successfully");
            } else {
                System.out.println("Unsupported operating system");
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void StartProcess(String path){
        try{
            if (this.os.contains("win")||this.os.contains("nux")||this.os.contains("nix")){
                this.runtime.exec(path);
                System.out.println("success");
            }else if (this.os.contains("mac")) {
                this.runtime.exec("open -n " + path);
                System.out.println("success");
            }else{ System.out.println("Unsuported Operating System"); }

        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
//    public static void main(String[] arg){
//        ProcessPC processPC = new ProcessPC();
//        processPC.StartProcess(" /System/Applications/Calculator.app");
//    }
}
