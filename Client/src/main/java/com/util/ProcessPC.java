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
    ProcessBuilder processBuilder;
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

    public boolean StartProcess(String path){
        try{
            if (this.os.contains("win")||this.os.contains("nux")||this.os.contains("nix")){
                this.processBuilder = new ProcessBuilder(path);
            }else if (this.os.contains("mac")) {
                this.processBuilder = new ProcessBuilder("open", "-n",path );
            }else{ System.out.println("Unsuported Operating System"); }
            Process process = processBuilder.start();
            if (process.isAlive()){
                System.out.println("succeed");
                return true;
            }else{
                System.out.println("failed");
                return false;
            }
        }catch(Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    public static void main(String[] args) {
        //System.out.println(ProcessPC.getInstance().ProcessList());
        ProcessPC.getInstance().StopProcess(8156);
    }

}
