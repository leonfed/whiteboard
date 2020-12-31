package ru.leonfed.whiteboard.client;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.util.List;

public interface MainController {

    void addShape(PaintShape shape);

    void addShapes(List<PaintShape> shapes);

    void refreshView();
}
