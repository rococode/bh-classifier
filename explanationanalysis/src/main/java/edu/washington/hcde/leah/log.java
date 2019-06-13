package edu.washington.hcde.leah;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class log {

    static File logFile = new File("./survey.log");

    public static void info(Object o) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        if (o == null) {
            o = "";
            System.err.println("Warning! Received null to log.info()");
        }
        String logmsg = timeStamp + " [info] " + o.toString();
        System.out.println(logmsg);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
            out.println(logmsg);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
