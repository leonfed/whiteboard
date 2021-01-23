package ru.leonfed.whiteboard.server.dao;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.time.Instant;
import java.util.List;

public interface PaintShapesDao {

    void createWhiteboard(String whiteboardId);

    void addShapes(String whiteboardId, String userId, List<PaintShape> shapes);

    List<PaintShape> getShapes(String whiteboardId, String excludeUserId, Instant after);
}
