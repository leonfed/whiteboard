package ru.leonfed.whiteboard.server.service;

import ru.leonfed.whiteboard.core.model.PaintShape;
import ru.leonfed.whiteboard.server.dao.PaintShapesDao;
import ru.leonfed.whiteboard.server.dao.WhiteboardDao;

import java.time.Instant;
import java.util.List;

public class WhiteboardServiceImpl implements WhiteboardService {

    private final WhiteboardDao whiteboardDao;
    private final PaintShapesDao shapesDao;

    public WhiteboardServiceImpl(WhiteboardDao whiteboardDao, PaintShapesDao shapesDao) {
        this.whiteboardDao = whiteboardDao;
        this.shapesDao = shapesDao;
    }

    @Override
    public String createWhiteboard() {
        String whiteboardId = whiteboardDao.createWhiteboard();
        shapesDao.createWhiteboard(whiteboardId);
        return whiteboardId;
    }

    @Override
    public String joinToWhiteboard(String whiteboardId) throws IllegalStateException {
        return whiteboardDao.joinWhiteboard(whiteboardId);
    }

    @Override
    public void addShapes(String whiteboardId, String userId, List<PaintShape> shapes) {
        shapesDao.addShapes(whiteboardId, userId, shapes);
    }

    @Override
    public List<PaintShape> getShapes(String whiteboardId, String excludeUserId, Instant after) {
        return shapesDao.getShapes(whiteboardId, excludeUserId, after);
    }
}
