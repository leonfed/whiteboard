package ru.leonfed.whiteboard.core;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    private static final long DEFAULT_INITIAL_DELAY = 100;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);

    public static void scheduleTask(Runnable task, Duration period) {
        Runnable modifiedTask = () -> {
            try {
                task.run();
            } catch (Throwable throwable) {
                //TODO use logging instead
                throwable.printStackTrace();
            }
        };

        scheduler.scheduleWithFixedDelay(modifiedTask, DEFAULT_INITIAL_DELAY, period.toMillis(), TimeUnit.MILLISECONDS);
    }
}
