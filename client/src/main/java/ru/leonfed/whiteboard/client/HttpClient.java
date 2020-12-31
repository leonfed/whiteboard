package ru.leonfed.whiteboard.client;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.time.Instant;
import java.util.List;

public interface HttpClient {

    /**
     * @return whiteboard id
     */
    int createWhiteboard();

    void joinToWhiteboard(int whiteboardId);

    List<PaintShape> getShapes(Instant after);

    void postShapes(List<PaintShape> shapes);
}
