package ru.leonfed.whiteboard.client.controller;

import org.json.JSONException;
import ru.leonfed.whiteboard.core.model.PaintShape;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public interface MainController {

    /**
     * Start with creating new whiteboard
     */
    void start() throws IOException, JSONException;

    /**
     * Start with joining to whiteboard
     */
    void start(String whiteboardId) throws IOException, JSONException;

    /**
     * Add shape to client storage
     */
    void addShape(PaintShape shape, boolean isOwn);

    /**
     * Add shapes to client storage
     */
    void addShapes(List<PaintShape> shapes, boolean isOwn);

    void refreshView();

    /**
     * @param after - timestamp for filtering shapes
     */
    void postShapesToServer(Instant after) throws IOException, JSONException;

    /**
     * @param after - timestamp for filtering shapes
     */
    void getShapesFromServer(Instant after) throws IOException, JSONException;
}
