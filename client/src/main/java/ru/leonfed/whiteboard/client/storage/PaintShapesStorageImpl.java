package ru.leonfed.whiteboard.client.storage;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaintShapesStorageImpl implements PaintShapesStorage {
    private final ConcurrentHashMap<String, RichPaintShape> ownShapes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, RichPaintShape> foreignShapes = new ConcurrentHashMap<>();

    @Override
    public void addShape(PaintShape shape, boolean isOwn) {
        RichPaintShape richPaintShape = new RichPaintShape(shape, Instant.now());
        if (isOwn) {
            ownShapes.putIfAbsent(shape.getId(), richPaintShape);
        } else {
            foreignShapes.putIfAbsent(shape.getId(), richPaintShape);
        }
    }

    @Override
    public List<PaintShape> getAllShapes() {
        Stream<RichPaintShape> ownShapesStream = ownShapes.values().stream();
        Stream<RichPaintShape> foreignShapesStream = foreignShapes.values().stream();
        return Stream.concat(ownShapesStream, foreignShapesStream)
                .map(RichPaintShape::getPaintShape)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaintShape> getOwnShapes(Instant after) {
        return ownShapes.values().stream()
                .filter(shape -> shape.createTime.isAfter(after))
                .map(RichPaintShape::getPaintShape)
                .collect(Collectors.toList());
    }

    static class RichPaintShape {
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

        public String getId() {
            return paintShape.getId();
        }
    }

}
