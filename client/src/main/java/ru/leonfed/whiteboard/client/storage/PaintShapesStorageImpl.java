package ru.leonfed.whiteboard.client.storage;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaintShapesStorageImpl implements PaintShapesStorage {
    private final HashMap<String, PaintShape> shapes = new HashMap<>();

    @Override
    public void addShape(PaintShape shape) {
        shapes.put(shape.getId(), shape);
    }

    @Override
    public List<PaintShape> getAllShapesCopy() {
        return new ArrayList<>(shapes.values());
    }
}
