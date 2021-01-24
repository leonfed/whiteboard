package ru.leonfed.whiteboard.client;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import ru.leonfed.whiteboard.client.controller.MainController;
import ru.leonfed.whiteboard.client.controller.MainControllerImpl;
import ru.leonfed.whiteboard.client.http.WhiteboardHttpClient;
import ru.leonfed.whiteboard.client.http.WhiteboardHttpClientImpl;
import ru.leonfed.whiteboard.client.storage.PaintShapesStorage;
import ru.leonfed.whiteboard.client.storage.PaintShapesStorageImpl;
import ru.leonfed.whiteboard.client.tasks.RefreshViewTask;
import ru.leonfed.whiteboard.client.tasks.SyncingShapesTask;
import ru.leonfed.whiteboard.client.view.View;
import ru.leonfed.whiteboard.client.view.ViewImpl;
import ru.leonfed.whiteboard.core.Scheduler;
import ru.leonfed.whiteboard.core.logging.Logger;
import ru.leonfed.whiteboard.core.logging.LoggerConfig;
import ru.leonfed.whiteboard.core.logging.LoggerConfig.LoggerLevel;
import ru.leonfed.whiteboard.core.logging.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

public class ClientInitializer {

    static final int DEFAULT_WIDTH = 500;
    static final int DEFAULT_HEIGHT = 500;
    static final Duration REFRESH_VIEW_TASK_PERIOD = Duration.ofMillis(50);
    static final Duration SYNCING_SHAPES_TASK_PERIOD = Duration.ofSeconds(1);

    static final LoggerLevel LOGGER_LEVEL = LoggerLevel.INFO;

    static final Logger log = LoggerFactory.logger(ClientInitializer.class);

    static void logUsageMessage() {
        log.error("Usage arguments:\n <server-hostname> <server-port> create\n <server-hostname> <server-port> join <whiteboard-id>");
    }

    public static void main(String[] args) {
        LoggerConfig.setLoggerLevel(LOGGER_LEVEL);

        String serverHostname;
        int serverPort;
        String whiteboardId = null;

        if (args.length >= 1 && args[0] != null) {
            serverHostname = args[0];
        } else {
            log.error("First argument must be server hostname");
            logUsageMessage();
            return;
        }
        if (args.length >= 2 && args[1] != null && StringUtils.isNumeric(args[1])) {
            serverPort = Integer.parseInt(args[1]);
        } else {
            log.error("Second argument must be server port");
            logUsageMessage();
            return;
        }

        if (args.length >= 4 && "join".equals(args[2])) {
            whiteboardId = args[3];
        } else if (args.length < 3 || !"create".equals(args[2])) {
            log.error("Incorrect mode");
            logUsageMessage();
            return;
        }

        log.info("Start client...");

        String url = "http://" + serverHostname + ":" + serverPort;
        log.debug("Server url is " + url);

        try {
            WhiteboardHttpClient whiteboardHttpClient = new WhiteboardHttpClientImpl(url);

            View view = new ViewImpl(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            PaintShapesStorage paintShapesStorage = new PaintShapesStorageImpl();
            MainController mainController = new MainControllerImpl(view, paintShapesStorage, whiteboardHttpClient);

            if (whiteboardId == null) {
                mainController.start();
            } else {
                mainController.start(whiteboardId);
            }

            RefreshViewTask refreshViewTask = new RefreshViewTask(mainController);
            Scheduler.scheduleTask(refreshViewTask, REFRESH_VIEW_TASK_PERIOD);

            SyncingShapesTask syncingShapesTask = new SyncingShapesTask(mainController);
            Scheduler.scheduleTask(syncingShapesTask, SYNCING_SHAPES_TASK_PERIOD);
        } catch (JSONException | IOException e) {
            log.error("Exception of initializing client", e);
        }
    }
}
