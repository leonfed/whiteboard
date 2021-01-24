package ru.leonfed.whiteboard.core.logging;

public class LoggerFactory {

    public static Logger logger(Class<?> classContext) {
        return logger(classContext.getName());
    }

    public static Logger logger(String context) {
        return new ConsoleLogger(context);
    }
}
