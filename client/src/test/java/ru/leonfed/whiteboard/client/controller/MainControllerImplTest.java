package ru.leonfed.whiteboard.client.controller;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.leonfed.whiteboard.client.http.WhiteboardHttpClient;
import ru.leonfed.whiteboard.client.storage.PaintShapesStorage;
import ru.leonfed.whiteboard.client.storage.PaintShapesStorageImpl;
import ru.leonfed.whiteboard.client.view.View;
import ru.leonfed.whiteboard.core.model.ModelGenerators;
import ru.leonfed.whiteboard.core.model.PaintShape;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MainControllerImplTest {

    View view = mock(View.class);
    WhiteboardHttpClient whiteboardHttpClient = mock(WhiteboardHttpClient.class);

    PaintShapesStorage paintShapesStorage;
    MainController controller;

    @Before
    public void initController() {
        paintShapesStorage = new PaintShapesStorageImpl();
        controller = new MainControllerImpl(view, paintShapesStorage, whiteboardHttpClient);
    }

    @Test
    public void addFewShapes() throws IOException, JSONException {
        PaintShape shape1 = ModelGenerators.generatePaintLine();
        PaintShape shape2 = ModelGenerators.generatePaintLine();
        List<PaintShape> shapes = List.of(shape1, shape2);
        controller.addShapes(shapes, true);
        controller.postShapesToServer(Instant.MIN);
        assertThat(paintShapesStorage.getAllShapes()).containsExactlyInAnyOrder(shape1, shape2);
    }

    @Test
    public void addOneShapeAndPostToServer() throws IOException, JSONException {
        PaintShape shape = ModelGenerators.generatePaintLine();
        controller.addShape(shape, true);
        controller.postShapesToServer(Instant.MIN);
        verify(whiteboardHttpClient).postShapes(List.of(shape));
    }

    @Test
    public void addFewShapesAndPostToServer() throws IOException, JSONException {
        PaintShape shape1 = ModelGenerators.generatePaintLine();
        PaintShape shape2 = ModelGenerators.generatePaintLine();
        List<PaintShape> shapes = List.of(shape1, shape2);
        controller.addShapes(shapes, true);
        controller.postShapesToServer(Instant.MIN);

        ArgumentCaptor<List<PaintShape>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(whiteboardHttpClient).postShapes(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).containsExactlyInAnyOrder(shape1, shape2);
    }

    @Test
    public void refreshView() {
        PaintShape shape1 = ModelGenerators.generatePaintLine();
        PaintShape shape2 = ModelGenerators.generatePaintLine();
        List<PaintShape> shapes = List.of(shape1, shape2);
        controller.addShapes(shapes, true);
        controller.refreshView();

        ArgumentCaptor<List<PaintShape>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(view).repaint(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).containsExactlyInAnyOrder(shape1, shape2);
    }

    @Test
    public void getShapesFromServer() throws IOException, JSONException {
        PaintShape shape1 = ModelGenerators.generatePaintLine();
        PaintShape shape2 = ModelGenerators.generatePaintLine();
        List<PaintShape> shapes = List.of(shape1, shape2);

        when(whiteboardHttpClient.getShapes(any())).thenReturn(shapes);

        controller.getShapesFromServer(Instant.MIN);

        assertThat(paintShapesStorage.getAllShapes()).containsExactlyInAnyOrder(shape1, shape2);
    }

    @Test
    public void start() throws IOException, JSONException {
        controller.start();
        verify(whiteboardHttpClient).createWhiteboard();
    }

    @Test
    public void startWithWhiteboardId() throws IOException, JSONException {
        String whiteboardId = UUID.randomUUID().toString();
        controller.start(whiteboardId);
        verify(whiteboardHttpClient).joinToWhiteboard(whiteboardId);
    }
}
