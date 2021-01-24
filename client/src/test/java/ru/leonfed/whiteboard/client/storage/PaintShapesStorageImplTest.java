package ru.leonfed.whiteboard.client.storage;

import org.junit.Before;
import org.junit.Test;
import ru.leonfed.whiteboard.core.model.PaintShape;
import ru.leonfed.whiteboard.core.model.ModelGenerators;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PaintShapesStorageImplTest {

    private PaintShapesStorageImpl paintShapesStorage;

    @Before
    public void initStorage() {
        paintShapesStorage = new PaintShapesStorageImpl();
    }


    @Test
    public void getAllShapes() {
        PaintShape shape1 = ModelGenerators.generatePaintLine();
        PaintShape shape2 = ModelGenerators.generatePaintLine();
        paintShapesStorage.addShape(shape1, true);
        paintShapesStorage.addShape(shape2, false);

        List<PaintShape> actualShapes = paintShapesStorage.getAllShapes();
        assertThat(actualShapes).containsExactlyInAnyOrder(shape1, shape2);
    }
}
