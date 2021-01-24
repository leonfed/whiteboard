package ru.leonfed.whiteboard.client.controller;

import org.json.JSONException;
import ru.leonfed.whiteboard.client.http.WhiteboardHttpClient;
import ru.leonfed.whiteboard.client.storage.PaintShapesStorage;
import ru.leonfed.whiteboard.client.view.View;
import ru.leonfed.whiteboard.core.model.PaintShape;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class MainControllerImpl implements MainController {
    private final PaintShapesStorage paintShapesStorage;
    private final View view;
    private final WhiteboardHttpClient whiteboardHttpClient;

    public MainControllerImpl(View view,
                              PaintShapesStorage paintShapesStorage,
                              WhiteboardHttpClient whiteboardHttpClient) {
        this.paintShapesStorage = paintShapesStorage;
        this.view = view;
        this.whiteboardHttpClient = whiteboardHttpClient;
        view.setAddShapeListener(shape -> addShape(shape, true));
    }

    @Override
    public void start() throws IOException, JSONException {
        whiteboardHttpClient.createWhiteboard();
        view.setVisible(true);
    }

    @Override
    public void start(String whiteboardId) throws IOException, JSONException {
        whiteboardHttpClient.joinToWhiteboard(whiteboardId);
        view.setVisible(true);
    }

    @Override
    public void addShape(PaintShape shape, boolean isOwn) {
        paintShapesStorage.addShape(shape, isOwn);
    }

    @Override
    public void addShapes(List<PaintShape> shapes, boolean isOwn) {
        shapes.forEach(shape -> addShape(shape, isOwn));
    }

    @Override
    public void refreshView() {
        List<PaintShape> shapes = paintShapesStorage.getAllShapes();
        view.repaint(shapes);
    }

    @Override
    public void postShapesToServer(Instant after) throws IOException, JSONException {
        List<PaintShape> shapes = paintShapesStorage.getOwnShapes(after);
        whiteboardHttpClient.postShapes(shapes);
    }

    @Override
    public void getShapesFromServer(Instant after) throws IOException, JSONException {
        List<PaintShape> shapes = whiteboardHttpClient.getShapes(after);
        addShapes(shapes, false);
    }
}
