package ru.leonfed.whiteboard.client;

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

    //todo move it to argument
    static final String url = "http://localhost:8080";

    //todo catch exceptions
    public static void main(String[] args) throws IOException, JSONException {
        System.out.println("It does something :|");

        WhiteboardHttpClient whiteboardHttpClient = new WhiteboardHttpClientImpl(url);
        if (args.length >= 1 && args[0].equals("create")) {
            whiteboardHttpClient.createWhiteboard();
        } else if (args.length >= 2 && args[0].equals("join")) {
            String whiteboardId = args[1];
            whiteboardHttpClient.joinToWhiteboard(whiteboardId);
        } else {
            System.out.println("Incorrect arguments");
            return;
        }

        View view = new ViewImpl(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        PaintShapesStorage paintShapesStorage = new PaintShapesStorageImpl();
        MainController mainController = new MainControllerImpl(view, paintShapesStorage, whiteboardHttpClient);

        RefreshViewTask refreshViewTask = new RefreshViewTask(mainController);
        Scheduler.scheduleTask(refreshViewTask, Duration.ofMillis(50));

        SyncingShapesTask syncingShapesTask = new SyncingShapesTask(mainController);
        Scheduler.scheduleTask(syncingShapesTask, Duration.ofSeconds(1));

        view.setVisible(true);
    }
}
