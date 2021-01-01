package ru.leonfed.whiteboard.client;

import ru.leonfed.whiteboard.client.controller.MainController;
import ru.leonfed.whiteboard.client.controller.MainControllerImpl;
import ru.leonfed.whiteboard.client.storage.PaintShapesStorage;
import ru.leonfed.whiteboard.client.storage.PaintShapesStorageImpl;
import ru.leonfed.whiteboard.client.tasks.RefreshViewTask;
import ru.leonfed.whiteboard.client.tasks.SyncingShapesTask;
import ru.leonfed.whiteboard.client.view.View;
import ru.leonfed.whiteboard.client.view.ViewImpl;
import ru.leonfed.whiteboard.core.Scheduler;

import java.time.Duration;

public class ClientInitializer {

    static final int DEFAULT_WIDTH = 500;
    static final int DEFAULT_HEIGHT = 500;

    public static void main(String[] args) {
        System.out.println("It does something :|");

        View view = new ViewImpl(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        PaintShapesStorage paintShapesStorage = new PaintShapesStorageImpl();
        MainController mainController = new MainControllerImpl(view, paintShapesStorage);

        RefreshViewTask refreshViewTask = new RefreshViewTask(mainController);
        Scheduler.scheduleTask(refreshViewTask, Duration.ofMillis(50));

        SyncingShapesTask syncingShapesTask = new SyncingShapesTask();
        Scheduler.scheduleTask(syncingShapesTask, Duration.ofSeconds(1));

        view.setVisible(true);
    }
}
