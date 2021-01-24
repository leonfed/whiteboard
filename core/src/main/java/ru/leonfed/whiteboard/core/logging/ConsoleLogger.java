package ru.leonfed.whiteboard.core.logging;

import org.apache.commons.lang.exception.ExceptionUtils;
import ru.leonfed.whiteboard.core.logging.LoggerConfig.LoggerLevel;

import java.time.Instant;

public class ConsoleLogger implements Logger {

    private final String context;

    public ConsoleLogger(String context) {
        this.context = context;
    }

    private void printMessage(LoggerLevel loggerLevel, String message) {
        if (LoggerConfig.needToLog(loggerLevel)) {
            String time = Instant.now().toString();
            System.out.println(time + " [" + context + "] (" + loggerLevel.toString() + ") " + message);
        }
    }

    @Override
    public void error(String message) {
        printMessage(LoggerLevel.ERROR, message);
    }

    @Override
    public void error(String message, Exception exception) {
        String stackTrace = ExceptionUtils.getStackTrace(exception);
        printMessage(LoggerLevel.ERROR, message + "\n" + stackTrace);
    }

    @Override
    public void info(String message) {
        printMessage(LoggerLevel.INFO, message);
    }

    @Override
    public void debug(String message) {
        printMessage(LoggerLevel.DEBUG, message);
    }
}
