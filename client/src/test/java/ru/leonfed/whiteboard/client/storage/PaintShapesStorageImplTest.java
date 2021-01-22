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
    public void addShapesAndGetCopy() {
        PaintShape shape1 = ModelGenerators.generatePaintShape();
        PaintShape shape2 = ModelGenerators.generatePaintShape();
        paintShapesStorage.addShape(shape1);
        paintShapesStorage.addShape(shape2);

        List<PaintShape> actualShapes = paintShapesStorage.getAllShapesCopy();
        assertThat(actualShapes).containsExactlyInAnyOrder(shape1, shape2);
    }
}
