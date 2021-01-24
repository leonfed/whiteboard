package ru.leonfed.whiteboard.server.service;

import org.junit.Test;
import ru.leonfed.whiteboard.core.model.ModelGenerators;
import ru.leonfed.whiteboard.core.model.PaintShape;
import ru.leonfed.whiteboard.server.dao.PaintShapesDao;
import ru.leonfed.whiteboard.server.dao.PaintShapesDaoImpl;
import ru.leonfed.whiteboard.server.dao.WhiteboardDao;
import ru.leonfed.whiteboard.server.dao.WhiteboardDaoImpl;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class WhiteboardServiceImplTest {

    @Test
    public void createWhiteboard() {
        WhiteboardDao whiteboardDao = mock(WhiteboardDao.class);
        PaintShapesDao paintShapesDao = mock(PaintShapesDao.class);
        WhiteboardService whiteboardService = new WhiteboardServiceImpl(whiteboardDao, paintShapesDao);

        String whiteboardId = "whiteboard-1";
        when(whiteboardDao.createWhiteboard()).thenReturn(whiteboardId);

        String actualWhiteboardId = whiteboardService.createWhiteboard();
        assertThat(actualWhiteboardId).isEqualTo(whiteboardId);
        verify(whiteboardDao).createWhiteboard();
        verify(paintShapesDao).createWhiteboard(whiteboardId);
    }

    @Test
    public void getShapes() {
        WhiteboardDao whiteboardDao = new WhiteboardDaoImpl();
        PaintShapesDao paintShapesDao = new PaintShapesDaoImpl();
        WhiteboardService whiteboardService = new WhiteboardServiceImpl(whiteboardDao, paintShapesDao);

        String whiteboardId = whiteboardService.createWhiteboard();
        String user = whiteboardService.joinToWhiteboard(whiteboardId);

        PaintShape shape1 = ModelGenerators.generatePaintLine();
        PaintShape shape2 = ModelGenerators.generatePaintLine();
        whiteboardService.addShapes(whiteboardId, user, List.of(shape1, shape2));

        List<PaintShape> paintShapes = whiteboardService.getShapes(whiteboardId, "another-user", Instant.MIN);
        assertThat(paintShapes).containsExactlyInAnyOrder(shape1, shape2);
    }

}
