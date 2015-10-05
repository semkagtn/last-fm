package com.semkagtn.musicdatamining.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by semkagtn on 05.10.15.
 */
public class LoggerUtils {

    private static int id = 0;

    public static Logger getUniqueFileLogger() {
        Handler fileHandler;
        try {
            fileHandler = new FileHandler(String.valueOf(++id));
        } catch (IOException e) {
            throw new LoggerUtilsError("Can't create file logger with name " + id);
        }
        fileHandler.setFormatter(new SimpleFormatter());
        Logger logger = Logger.getLogger(String.valueOf(id));
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
        return logger;
    }

    private LoggerUtils() {

    }

    private static class LoggerUtilsError extends Error {

        public LoggerUtilsError() {

        }

        public LoggerUtilsError(String message) {
            super(message);
        }

        public LoggerUtilsError(Throwable cause) {
            super(cause);
        }
    }
}
