package Homework.utilities;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

    private static Logger myLogger = null;

    public static void init() {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s: %5$s [%1$tc]%n");
        myLogger = Logger.getLogger("MyLog");
        FileHandler fh;
        try {
            fh = new FileHandler("src/Homework/utilities/logs.log");
            myLogger.addHandler(fh);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fh.setFormatter(simpleFormatter);
            myLogger.info("Logger initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Logger getMyLogger() {
        return myLogger;
    }
}
