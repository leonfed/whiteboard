package ru.leonfed.whiteboard.client.storage;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaintShapesStorageImpl implements PaintShapesStorage {
    private final ConcurrentHashMap<String, RichPaintShape> ownShapesById = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, RichPaintShape> foreignShapesById = new ConcurrentHashMap<>();

    @Override
    public void addShape(PaintShape shape, boolean isOwn) {
        RichPaintShape richPaintShape = new RichPaintShape(shape, Instant.now());
        if (isOwn) {
            ownShapesById.putIfAbsent(shape.getId(), richPaintShape);
        } else {
            foreignShapesById.putIfAbsent(shape.getId(), richPaintShape);
        }
    }

    @Override
    public List<PaintShape> getAllShapes() {
        Stream<RichPaintShape> ownShapesStream = ownShapesById.values().stream();
        Stream<RichPaintShape> foreignShapesStream = foreignShapesById.values().stream();
        return Stream.concat(ownShapesStream, foreignShapesStream)
                .map(RichPaintShape::getPaintShape)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaintShape> getOwnShapes(Instant after) {
        return ownShapesById.values().stream()
                .filter(shape -> shape.getCreateTime().isAfter(after))
                .map(RichPaintShape::getPaintShape)
                .collect(Collectors.toList());
    }

    public static class RichPaintShape {
        private final PaintShape paintShape;
        private final Instant createTime;

        public RichPaintShape(PaintShape paintShape, Instant createTime) {
            this.paintShape = paintShape;
            this.createTime = createTime;
        }

        public PaintShape getPaintShape() {
            return paintShape;
        }

        public Instant getCreateTime() {
            return createTime;
        }
    }

}
