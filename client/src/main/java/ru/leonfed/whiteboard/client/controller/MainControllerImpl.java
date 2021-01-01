package ru.leonfed.whiteboard.client.controller;

import ru.leonfed.whiteboard.client.storage.PaintShapesStorage;
import ru.leonfed.whiteboard.client.view.View;
import ru.leonfed.whiteboard.core.model.PaintShape;

import java.util.List;

public class MainControllerImpl implements MainController {
    private final PaintShapesStorage paintShapesStorage;
    private final View view;

    public MainControllerImpl(View view, PaintShapesStorage paintShapesStorage) {
        this.paintShapesStorage = paintShapesStorage;
        this.view = view;
        view.setAddShapeListener(this::addShape);
    }

    @Override
    public void addShape(PaintShape shape) {
        paintShapesStorage.addShape(shape);
    }

    @Override
    public void addShapes(List<PaintShape> shapes) {
        shapes.forEach(this::addShape);
    }

    @Override
    public void refreshView() {
        List<PaintShape> shapes = paintShapesStorage.getAllShapesCopy();
        view.repaint(shapes);
    }
}
