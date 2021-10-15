package com.hpi.operations;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class icmpCheck {


    public static void main(String[] args) throws IOException {

        String[] Server = {"g8t01604a.inc.hpicorp.net","g7t01970a.inc.hpicorp.net"};
            System.out.println("currently active " + isReachablebyPing(Server[0]));
            System.out.println("decommissioned " + isReachablebyPing(Server[1]));

        }
    public static boolean isReachablebyPing(String ip) {

        try {
            String command;

            if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                // For Windows
                command = "ping -n 2 " + ip;
            } else {
                // For Linux and OSX
                command = "ping -c 2 " + ip;
            }

            Process proc = Runtime.getRuntime().exec(command);
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
            outputGobbler.start();

            proc.waitFor();
            return checkAvailability(outputGobbler.getOutputLines());

        } catch(IOException | InterruptedException ex) {
            Logger.getLogger(StreamGobbler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }


    private static boolean checkAvailability(List<String> outputLines) {

        for(String line : outputLines) {
            if(line.contains("unreachable")) {
                return false;
            }
            if(line.contains("TTL=")) {
                return true;
            }
        }
        return false;

    }

}

class StreamGobbler extends Thread {

    protected InputStream is;

    protected String type;

    protected List<String> outputLines;

    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
        outputLines = new ArrayList<>();
    }


    public List<String> getOutputLines() {
        return outputLines;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null) {
                outputLines.add(line);
            }
        } catch(IOException ex) {
            Logger.getLogger(StreamGobbler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }




    }
