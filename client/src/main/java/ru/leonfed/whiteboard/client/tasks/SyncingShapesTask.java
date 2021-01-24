package ru.leonfed.whiteboard.client.tasks;

import org.json.JSONException;
import ru.leonfed.whiteboard.client.controller.MainController;
import ru.leonfed.whiteboard.core.logging.Logger;
import ru.leonfed.whiteboard.core.logging.LoggerFactory;

import java.io.IOException;
import java.time.Instant;

/**
 * It does two steps
 * 1) Post client's new shapes to server
 * 2) Get server's new shapes from other users and store it
 */
public class SyncingShapesTask implements Runnable {
    private static final long OVERLAP_SECONDS_POST = 60;
    private static final long OVERLAP_SECONDS_GET = 300;

    static final Logger log = LoggerFactory.logger(SyncingShapesTask.class);

    private Instant postTimestamp = Instant.MIN;
    private Instant getTimestamp = Instant.MIN;
    private final MainController controller;

    public SyncingShapesTask(MainController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            Instant executeTime = Instant.now();
            controller.postShapesToServer(postTimestamp);
            controller.getShapesFromServer(getTimestamp);

            postTimestamp = executeTime.minusSeconds(OVERLAP_SECONDS_POST);
            getTimestamp = executeTime.minusSeconds(OVERLAP_SECONDS_GET);
        } catch (IOException | JSONException exception) {
            log.error("Exception of running SyncingShapesTask", exception);
        }
    }
}
