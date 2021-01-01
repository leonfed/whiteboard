package ru.leonfed.whiteboard.client.storage;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.util.List;

public interface PaintShapesStorage {

    void addShape(PaintShape shape);

    List<PaintShape> getAllShapesCopy();
}
