package com.util;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
public class ProcessPC {
    String os;
    Runtime runtime;

    public ProcessPC(){
        try {
            this.os = System.getProperty("os.name").toLowerCase();
            this.runtime = Runtime.getRuntime();
        }catch (Exception e){ System.out.println(e.toString());}
    }

    public void ProcessList(){
        try{
            Process process = null;
            if (this.os.contains("win")) { process = runtime.exec("tasklist");}
            else if (this.os.contains("mac")||this.os.contains("nux") || this.os.contains("nix")) { process = runtime.exec("ps aux");}
            if (process!=null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new FileWriter("process.txt"));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.close();
                reader.close();
            }else{ System.out.println("Unsupported operating system");}
        }catch (Exception e){ System.out.println(e.toString()); }
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
