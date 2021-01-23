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

import java.io.IOException;
import java.time.Duration;

public class ClientInitializer {

    static final int DEFAULT_WIDTH = 500;
    static final int DEFAULT_HEIGHT = 500;

    static void printUsageMessage() {
        System.out.println("Usage arguments:\n <server-hostname> <server-port> create\n <server-hostname> <server-port> join <whiteboard-id>");
    }

    public static void main(String[] args) {
        String serverHostname;
        int serverPort;
        String whiteboardId = null;

        if (args.length >= 1 && args[0] != null) {
            serverHostname = args[0];
        } else {
            System.out.println("First argument must be server hostname");
            printUsageMessage();
            return;
        }
        if (args.length >= 2 && args[1] != null && StringUtils.isNumeric(args[1])) {
            serverPort = Integer.parseInt(args[1]);
        } else {
            System.out.println("Second argument must be server port");
            printUsageMessage();
            return;
        }

        if (args.length >= 4 && "join".equals(args[2])) {
            whiteboardId = args[3];
        } else if (args.length < 3 || !"create".equals(args[2])) {
            System.out.println("Incorrect mode");
            printUsageMessage();
            return;
        }

        String url = "http://" + serverHostname + ":" + serverPort;

        System.out.println("Start client...");

        try {
            WhiteboardHttpClient whiteboardHttpClient = new WhiteboardHttpClientImpl(url);

            if (whiteboardId == null) {
                whiteboardHttpClient.createWhiteboard();
            } else {
                whiteboardHttpClient.joinToWhiteboard(whiteboardId);
            }
            View view = new ViewImpl(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            PaintShapesStorage paintShapesStorage = new PaintShapesStorageImpl();
            MainController mainController = new MainControllerImpl(view, paintShapesStorage, whiteboardHttpClient);

            RefreshViewTask refreshViewTask = new RefreshViewTask(mainController);
            Scheduler.scheduleTask(refreshViewTask, Duration.ofMillis(50));

            SyncingShapesTask syncingShapesTask = new SyncingShapesTask(mainController);
            Scheduler.scheduleTask(syncingShapesTask, Duration.ofSeconds(1));

            view.setVisible(true);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
