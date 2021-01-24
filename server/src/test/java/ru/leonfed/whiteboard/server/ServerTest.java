package ru.leonfed.whiteboard.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import ru.leonfed.whiteboard.core.model.JsonConverter;
import ru.leonfed.whiteboard.core.model.ModelGenerators;
import ru.leonfed.whiteboard.core.model.PaintShape;
import ru.leonfed.whiteboard.server.service.WhiteboardService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

public class ServerTest {

    private static final AtomicInteger port = new AtomicInteger(3000);

    WhiteboardService whiteboardService = mock(WhiteboardService.class);
    Server server;

    @Before
    public void initDao() throws IOException {
        server = new Server("localhost", port.incrementAndGet(), whiteboardService);
    }

    private HttpExchange mockHttpExchange() {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getResponseHeaders()).thenReturn(mock(Headers.class));
        when(httpExchange.getResponseBody()).thenReturn(mock(OutputStream.class));
        return httpExchange;
    }

    @Test
    public void handleCreateWhiteboard() throws IOException {
        String whiteboardId = UUID.randomUUID().toString();
        when(whiteboardService.createWhiteboard()).thenReturn(whiteboardId);
        HttpExchange httpExchange = mockHttpExchange();

        Server.CreateWhiteboardHandler createWhiteboardHandler = server.new CreateWhiteboardHandler();
        createWhiteboardHandler.handle(httpExchange);

        verify(whiteboardService).createWhiteboard();
        verify(whiteboardService).joinToWhiteboard(whiteboardId);
        verify(httpExchange).sendResponseHeaders(eq(200), anyLong());
    }

    @Test
    public void handleJoinWhiteboard() throws IOException, URISyntaxException {
        String whiteboardId = UUID.randomUUID().toString();
        HttpExchange httpExchange = mockHttpExchange();
        when(httpExchange.getRequestURI()).thenReturn(new URI("?whiteboard=" + whiteboardId));

        Server.JoinWhiteboardHandler joinWhiteboardHandler = server.new JoinWhiteboardHandler();
        joinWhiteboardHandler.handle(httpExchange);

        verify(whiteboardService).joinToWhiteboard(whiteboardId);
        verify(httpExchange).sendResponseHeaders(eq(200), anyLong());
    }

    @Test
    public void handleJoinWhiteboardWithBadRequest() throws IOException, URISyntaxException {
        HttpExchange httpExchange = mockHttpExchange();
        when(httpExchange.getRequestURI()).thenReturn(new URI(""));

        Server.JoinWhiteboardHandler joinWhiteboardHandler = server.new JoinWhiteboardHandler();
        joinWhiteboardHandler.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(400), anyLong());
    }

    @Test
    public void handleGetShapes() throws IOException, URISyntaxException {
        String whiteboardId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Instant after = Instant.MIN;
        HttpExchange httpExchange = mockHttpExchange();
        when(httpExchange.getRequestURI()).thenReturn(
                new URI("?whiteboard=" + whiteboardId + "&user=" + userId + "&after=" + after.toString())
        );

        Server.GetShapesHandler joinWhiteboardHandler = server.new GetShapesHandler();
        joinWhiteboardHandler.handle(httpExchange);

        verify(whiteboardService).getShapes(whiteboardId, userId, after);
        verify(httpExchange).sendResponseHeaders(eq(200), anyLong());
    }

    @Test
    public void handleGetShapesWithBadRequest() throws IOException, URISyntaxException {
        HttpExchange httpExchange = mockHttpExchange();
        when(httpExchange.getRequestURI()).thenReturn(new URI(""));

        Server.GetShapesHandler getShapesHandler = server.new GetShapesHandler();
        getShapesHandler.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(400), anyLong());
    }

    @Test
    public void handleAddShapes() throws IOException, URISyntaxException, JSONException {
        String whiteboardId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        HttpExchange httpExchange = mockHttpExchange();
        when(httpExchange.getRequestURI()).thenReturn(new URI("?whiteboard=" + whiteboardId + "&user=" + userId));

        List<PaintShape> paintShapes = List.of(ModelGenerators.generatePaintLine());
        String jsonPaintShapes = JsonConverter.toJsonPaintShapes(paintShapes).toString();
        when(httpExchange.getRequestBody()).thenReturn(new ByteArrayInputStream(jsonPaintShapes.getBytes()));

        Server.AddShapesHandler addShapesHandler = server.new AddShapesHandler();
        addShapesHandler.handle(httpExchange);

        verify(whiteboardService).addShapes(whiteboardId, userId, paintShapes);
        verify(httpExchange).sendResponseHeaders(eq(200), anyLong());
    }

    @Test
    public void handleAddShapesWithIncorrectJson() throws IOException, URISyntaxException {
        String whiteboardId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        HttpExchange httpExchange = mockHttpExchange();
        when(httpExchange.getRequestURI()).thenReturn(new URI("?whiteboard=" + whiteboardId + "&user=" + userId));
        when(httpExchange.getRequestBody()).thenReturn(new ByteArrayInputStream("".getBytes()));

        Server.AddShapesHandler addShapesHandler = server.new AddShapesHandler();
        addShapesHandler.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(400), anyLong());
    }

    @Test
    public void handleAddShapesWithBadRequest() throws IOException, URISyntaxException {
        HttpExchange httpExchange = mockHttpExchange();
        when(httpExchange.getRequestURI()).thenReturn(new URI(""));

        Server.AddShapesHandler addShapesHandler = server.new AddShapesHandler();
        addShapesHandler.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(400), anyLong());
    }
}
