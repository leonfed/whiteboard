package ru.leonfed.whiteboard.core.logging;

public interface Logger {
    void info(String message);

    void error(String message, Exception exception);
}
