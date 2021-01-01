package ru.leonfed.whiteboard.client.tasks;

import ru.leonfed.whiteboard.client.controller.MainController;

public class RefreshViewTask implements Runnable {
    private final MainController controller;

    public RefreshViewTask(MainController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        controller.refreshView();
    }
}
