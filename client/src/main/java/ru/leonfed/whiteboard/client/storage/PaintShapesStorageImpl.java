package ru.leonfed.whiteboard.client.storage;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PaintShapesStorageImpl implements PaintShapesStorage {
    private final ConcurrentHashMap<String, PaintShape> shapes = new ConcurrentHashMap<>();

    @Override
    public void addShape(PaintShape shape) {
        shapes.put(shape.getId(), shape);
    }

    @Override
    public List<PaintShape> getAllShapesCopy() {
        return new ArrayList<>(shapes.values());
    }
}
