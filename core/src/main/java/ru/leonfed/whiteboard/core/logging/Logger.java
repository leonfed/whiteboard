package ru.leonfed.whiteboard.core.logging;

public interface Logger {
    void error(String message);

    void error(String message, Exception exception);

    void info(String message);

    void debug(String message);
}
