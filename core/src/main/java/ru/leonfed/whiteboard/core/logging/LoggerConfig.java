package ru.leonfed.whiteboard.core.logging;

public class LoggerConfig {

    public enum LoggerLevel {ERROR, INFO, DEBUG}

    private static LoggerLevel configLoggerLevel = LoggerLevel.INFO;

    public static void setLoggerLevel(LoggerLevel configLoggerLevel) {
        LoggerConfig.configLoggerLevel = configLoggerLevel;
    }

    public static boolean needToLog(LoggerLevel loggerLevel) {
        boolean needFlag = false;
        switch (configLoggerLevel) {
            case ERROR:
                needFlag = loggerLevel == LoggerLevel.ERROR;
                break;
            case INFO:
                needFlag = loggerLevel == LoggerLevel.ERROR || loggerLevel == LoggerLevel.INFO;
                break;
            case DEBUG:
                needFlag = true;
                break;
        }
        return needFlag;
    }
}
