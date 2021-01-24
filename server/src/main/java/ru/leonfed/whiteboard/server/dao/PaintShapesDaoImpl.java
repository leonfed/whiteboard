package ru.leonfed.whiteboard.server.dao;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PaintShapesDaoImpl implements PaintShapesDao {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, RichPaintShape>> shapes = new ConcurrentHashMap<>();

    @Override
    public void createWhiteboard(String whiteboardId) {
        shapes.put(whiteboardId, new ConcurrentHashMap<>());
    }

    @Override
    public void addShapes(String whiteboardId, String userId, List<PaintShape> paintShapes) {
        Instant createTime = Instant.now();

        ConcurrentHashMap<String, RichPaintShape> whiteboardShapes = shapes.get(whiteboardId);

        paintShapes.stream()
                .map(shape -> new RichPaintShape(shape, createTime, userId))
                .forEach(shape -> whiteboardShapes.putIfAbsent(shape.getId(), shape));
    }

    @Override
    public List<PaintShape> getShapes(String whiteboardId, String excludeUserId, Instant after) {
        return shapes.get(whiteboardId).values()
                .stream()
                .filter(shape -> !shape.getCreatorUserId().equals(excludeUserId) && shape.getCreateTime().isAfter(after))
                .map(RichPaintShape::getPaintShape)
                .collect(Collectors.toList());
    }

    static class RichPaintShape {
        private final PaintShape paintShape;
        private final Instant createTime;
        private final String creatorUserId;

        public RichPaintShape(PaintShape paintShape, Instant createTime, String creatorUserId) {
            this.paintShape = paintShape;
            this.createTime = createTime;
            this.creatorUserId = creatorUserId;
        }

        public PaintShape getPaintShape() {
            return paintShape;
        }

        public Instant getCreateTime() {
            return createTime;
        }

        public String getCreatorUserId() {
            return creatorUserId;
        }

        public String getId() {
            return paintShape.getId();
        }
    }

}
