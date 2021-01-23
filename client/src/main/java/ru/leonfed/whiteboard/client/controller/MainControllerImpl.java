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
    private static final long OVERLAP_SECONDS = 60;

    private final PaintShapesStorage paintShapesStorage;
    private final View view;
    private final WhiteboardHttpClient whiteboardHttpClient;

    //todo move it to storage
    private Instant postTimestamp = Instant.MIN;
    private Instant getTimestamp = Instant.MIN;

    public MainControllerImpl(View view,
                              PaintShapesStorage paintShapesStorage,
                              WhiteboardHttpClient whiteboardHttpClient) {
        this.paintShapesStorage = paintShapesStorage;
        this.view = view;
        this.whiteboardHttpClient = whiteboardHttpClient;
        view.setAddShapeListener(shape -> addShape(shape, true));
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
    public void postShapesToServer() throws IOException, JSONException {
        Instant executeTime = Instant.now();
        List<PaintShape> shapes = paintShapesStorage.getOwnShapes(postTimestamp);
        whiteboardHttpClient.postShapes(shapes);
        postTimestamp = executeTime.minusSeconds(OVERLAP_SECONDS);
    }

    @Override
    public void getShapesFromServer() throws IOException, JSONException {
        Instant executeTime = Instant.now();
        List<PaintShape> shapes = whiteboardHttpClient.getShapes(getTimestamp);
        addShapes(shapes, false);
        getTimestamp = executeTime.minusSeconds(OVERLAP_SECONDS * 5);
    }
}
