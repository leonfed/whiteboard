package ru.leonfed.whiteboard.client;

import ru.leonfed.whiteboard.core.model.PaintShape;

public interface PaintShapesStorage {

    void addShape(PaintShape shape);

    void getAllShapesCopy();
}
