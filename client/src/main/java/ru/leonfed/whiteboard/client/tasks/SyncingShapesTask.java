package ru.leonfed.whiteboard.client.tasks;

import org.json.JSONException;
import ru.leonfed.whiteboard.client.controller.MainController;

import java.io.IOException;

/**
 * It does two steps
 * 1) Post client's new shapes to server
 * 2) Get server's new shapes from other users and store it
 */
public class SyncingShapesTask implements Runnable {
    private final MainController controller;

    public SyncingShapesTask(MainController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        //todo use logging
        System.out.println("Run SyncingShapesTask");

        try {
            controller.postShapesToServer();
            controller.getShapesFromServer();
        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
        }
    }
}
