package ru.leonfed.whiteboard.server.dao;

import org.junit.Before;
import org.junit.Test;
import ru.leonfed.whiteboard.core.model.ModelGenerators;
import ru.leonfed.whiteboard.core.model.PaintShape;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PaintShapesDaoImplTest {

    PaintShapesDao paintShapesDao;

    @Before
    public void initDao() {
        paintShapesDao = new PaintShapesDaoImpl();
    }

    @Test
    public void getShapes() {
        String whiteboardId = "whiteboard-1";
        String user = "user-1";
        PaintShape shape1 = ModelGenerators.generatePaintLine();
        PaintShape shape2 = ModelGenerators.generatePaintLine();
        PaintShape shape3 = ModelGenerators.generatePaintLine();

        paintShapesDao.createWhiteboard(whiteboardId);
        paintShapesDao.addShapes(whiteboardId, user, List.of(shape1, shape2));
        paintShapesDao.addShapes(whiteboardId, user, List.of(shape3));

        List<PaintShape> paintShapes = paintShapesDao.getShapes(whiteboardId, "another-user", Instant.MIN);
        assertThat(paintShapes).containsExactlyInAnyOrder(shape1, shape2, shape3);
    }

    @Test
    public void getShapesFromOnlyOneWhiteboard() {
        String whiteboardId1 = "whiteboard-1";
        String whiteboardId2 = "whiteboard-2";
        String user = "user-1";
        PaintShape shape1 = ModelGenerators.generatePaintLine();
        PaintShape shape2 = ModelGenerators.generatePaintLine();

        paintShapesDao.createWhiteboard(whiteboardId1);
        paintShapesDao.createWhiteboard(whiteboardId2);
        paintShapesDao.addShapes(whiteboardId1, user, List.of(shape1));
        paintShapesDao.addShapes(whiteboardId2, user, List.of(shape2));

        List<PaintShape> paintShapes = paintShapesDao.getShapes(whiteboardId1, "another-user", Instant.MIN);
        assertThat(paintShapes).containsExactlyInAnyOrder(shape1);
    }

    @Test
    public void getShapesWithExcludedUser() {
        String whiteboardId = "whiteboard-1";
        String user1 = "user-1";
        String user2 = "user-2";
        PaintShape shape1 = ModelGenerators.generatePaintLine();
        PaintShape shape2 = ModelGenerators.generatePaintLine();

        paintShapesDao.createWhiteboard(whiteboardId);
        paintShapesDao.addShapes(whiteboardId, user1, List.of(shape1));
        paintShapesDao.addShapes(whiteboardId, user2, List.of(shape2));

        List<PaintShape> paintShapes = paintShapesDao.getShapes(whiteboardId, user1, Instant.MIN);
        assertThat(paintShapes).containsExactlyInAnyOrder(shape2);
    }

    @Test
    public void getShapesAfterTimestamp() {
        String whiteboardId = "whiteboard-1";
        String user = "user-1";
        PaintShape shape1 = ModelGenerators.generatePaintLine();
        PaintShape shape2 = ModelGenerators.generatePaintLine();

        paintShapesDao.createWhiteboard(whiteboardId);
        paintShapesDao.addShapes(whiteboardId, user, List.of(shape1));
        Instant timestamp = Instant.now();
        paintShapesDao.addShapes(whiteboardId, user, List.of(shape2));

        List<PaintShape> paintShapes = paintShapesDao.getShapes(whiteboardId, "another-user", timestamp);
        assertThat(paintShapes).containsExactlyInAnyOrder(shape2);
    }

}
