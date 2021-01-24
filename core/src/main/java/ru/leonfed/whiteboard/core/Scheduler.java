package ru.leonfed.whiteboard.core;

import ru.leonfed.whiteboard.core.logging.Logger;
import ru.leonfed.whiteboard.core.logging.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    static final Logger log = LoggerFactory.logger(Scheduler.class);

    private static final long DEFAULT_INITIAL_DELAY = 100;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);

    public static void scheduleTask(Runnable task, Duration period) {
        Runnable modifiedTask = () -> {
            try {
                log.debug("Starting task: " + task.getClass().getName());
                task.run();
            } catch (Exception exception) {
                log.error("Exception of running task", exception);
            }
        };

        scheduler.scheduleWithFixedDelay(modifiedTask, DEFAULT_INITIAL_DELAY, period.toMillis(), TimeUnit.MILLISECONDS);
    }
}
