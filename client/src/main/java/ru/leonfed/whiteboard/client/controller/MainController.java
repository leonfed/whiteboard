package ru.leonfed.whiteboard.client.controller;

import org.json.JSONException;
import ru.leonfed.whiteboard.core.model.PaintShape;

import java.io.IOException;
import java.util.List;

public interface MainController {

    void addShape(PaintShape shape, boolean isOwn);

    void addShapes(List<PaintShape> shapes, boolean isOwn);

    void refreshView();

    void postShapesToServer() throws IOException, JSONException;

    void getShapesFromServer() throws IOException, JSONException;
}
