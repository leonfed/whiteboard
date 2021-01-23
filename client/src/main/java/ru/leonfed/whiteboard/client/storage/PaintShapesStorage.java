package ru.leonfed.whiteboard.client.storage;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.time.Instant;
import java.util.List;

public interface PaintShapesStorage {

    void addShape(PaintShape shape, boolean isOwn);

    List<PaintShape> getAllShapes();

    List<PaintShape> getOwnShapes(Instant after);
}
