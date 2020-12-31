package ru.leonfed.whiteboard.core.logging;

public class ConsoleLogger implements Logger {
    @Override
    public void info(String message) {
        System.out.println(message);
    }

    @Override
    public void error(String message, Exception exception) {
        System.err.println(message);
        exception.printStackTrace();
    }
}
