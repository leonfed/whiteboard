package ru.leonfed.whiteboard.client.http;

import org.json.JSONException;
import ru.leonfed.whiteboard.core.model.PaintShape;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public interface WhiteboardHttpClient {

    void createWhiteboard() throws IOException, JSONException;

    void joinToWhiteboard(String whiteboardId) throws IOException, JSONException;

    List<PaintShape> getShapes(Instant after) throws IOException, JSONException;

    void postShapes(List<PaintShape> shapes) throws IOException, JSONException;
}
