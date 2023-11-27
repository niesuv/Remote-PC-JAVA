package com.util;

public class ShutandLog {
    Runtime runtime;
    String os; //The Server's Operating system
    private static ShutandLog instance = new ShutandLog();
    public static ShutandLog getInstance(){return instance;}
    public void Respond(int exitcode){
        if (exitcode == 0) {
            System.out.println("Shut down successfully");
        }else if(exitcode == -1){
            System.out.println("Unsupported operating system");
        } else {
            System.out.println("Shutdown fail. Exit code: "+exitcode);
        }
    }
    public ShutandLog(){//Constructor
        try {
            this.runtime = Runtime.getRuntime();
            this.os = System.getProperty("os.name").toLowerCase();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
    public int Shutdown(int time, String Password){
        try {
            Process process;
            int exitcode = -1; // Exit code for checking whether Shutdown is successful
            if(this.os.contains("win")) {
                process = this.runtime.exec("shutdown /s /f /t " + Integer.toString(time));
                exitcode = process.waitFor();
            }
            else if(this.os.contains("nux")||this.os.contains("nix")||this.os.contains("mac")){
                String command = "echo '" + Password + "' | sudo -S shutdown -h "+Integer.toString(time);
                process = Runtime.getRuntime().exec(new String[] { "/bin/bash","-c", command });
                exitcode = process.waitFor();
            }

            Respond(exitcode);
            return exitcode;

        }catch (Exception e){
            System.out.println(e.toString());
            return 1;
        }
    }

    public void Restart( String Password){
        try {
            Process process;
            int exitcode = -1;
            if(this.os.contains("win")) {
                process = this.runtime.exec("shutdown /r /f /t 0");
                exitcode = process.waitFor();
            }
            else if(this.os.contains("nux")||this.os.contains("nix")||this.os.contains("mac")){
                String command = "echo '" + Password + "' | sudo -S reboot";
                process = Runtime.getRuntime().exec(new String[] { "/bin/bash","-c", command });
                exitcode = process.waitFor();
            }

            Respond(exitcode);

        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public int Logout(String Password){
        try {
            Process process;
            int exitcode = -1; // Exit code for checking whether Shutdown is successful
            if(this.os.contains("win")) {
                process = this.runtime.exec("shutdown -s -f -t 0");
                exitcode = process.waitFor();
            }
            else if(this.os.contains("nux")||this.os.contains("nix")||this.os.contains("mac")){
                String command = "echo '" + Password + "' | sudo -S shutdown -h now";
                process = Runtime.getRuntime().exec(new String[] { "/bin/bash","-c", command });
                exitcode = process.waitFor();
            }

            Respond(exitcode);
            return exitcode;

        }catch (Exception e){
            System.out.println(e.toString());
            return 1;
        }

    }

    public static void main(String[] arg){
        ShutandLog shutandLog = new ShutandLog();
        shutandLog.Logout("");
    }
}
