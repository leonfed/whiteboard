package ru.leonfed.whiteboard.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import ru.leonfed.whiteboard.core.logging.Logger;
import ru.leonfed.whiteboard.core.logging.LoggerFactory;
import ru.leonfed.whiteboard.core.model.JsonConverter;
import ru.leonfed.whiteboard.core.model.PaintShape;
import ru.leonfed.whiteboard.server.service.WhiteboardService;
import ru.leonfed.whiteboard.server.http.HttpUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Server {
    private static final int BACKLOG = 10;

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    static Logger log = LoggerFactory.logger(Server.class);

    private final WhiteboardService whiteboardService;
    private final HttpServer httpServer;

    public Server(String hostname, int port, WhiteboardService whiteboardService) throws IOException {
        this.whiteboardService = whiteboardService;
        this.httpServer = HttpServer.create(new InetSocketAddress(hostname, port), BACKLOG);
        httpServer.createContext("/whiteboard/create", new CreateWhiteboardHandler());
        httpServer.createContext("/whiteboard/join", new JoinWhiteboardHandler());
        httpServer.createContext("/shapes/get", new GetShapesHandler());
        httpServer.createContext("/shapes/add", new AddShapesHandler());
    }

    void start() {
        httpServer.start();
    }

    private class CreateWhiteboardHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            log.debug("Start handle request by CreateWhiteboardHandler");

            try {
                String whiteboardId = whiteboardService.createWhiteboard();
                String userId = whiteboardService.joinToWhiteboard(whiteboardId);

                log.info("Create whiteboard [" + whiteboardId + "] and new user [" + userId + "]");

                String json = new JSONObject().put("whiteboard", whiteboardId).put("user", userId).toString();
                HttpUtils.sendJson(httpExchange, json);
            } catch (JSONException | IllegalStateException exception) {
                HttpUtils.sendBadRequestCode(httpExchange);
            } finally {
                httpExchange.close();
            }
        }
    }

    private class JoinWhiteboardHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            log.debug("Start handle request by JoinWhiteboardHandler");

            try {
                Map<String, String> queryParams = HttpUtils.getQueryParams(httpExchange.getRequestURI());

                String whiteboardId = HttpUtils.getParam(queryParams, "whiteboard");
                String userId = whiteboardService.joinToWhiteboard(whiteboardId);

                log.info("Join new user [" + userId + "] to whiteboard [" + whiteboardId + "]");

                String json = new JSONObject().put("user", userId).toString();
                HttpUtils.sendJson(httpExchange, json);
            } catch (JSONException | IllegalStateException exception) {
                HttpUtils.sendBadRequestCode(httpExchange);
            } finally {
                httpExchange.close();
            }
        }
    }

    private class GetShapesHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            log.debug("Start handle request by GetShapesHandler");

            try {
                Map<String, String> queryParams = HttpUtils.getQueryParams(httpExchange.getRequestURI());

                String whiteboardId = HttpUtils.getParam(queryParams, "whiteboard");
                String userId = HttpUtils.getParam(queryParams, "user");
                Instant after = Instant.parse(HttpUtils.getParam(queryParams, "after"));

                log.debug("Get shapes of whiteboard [" + whiteboardId + "] by user [" + userId + "]");

                List<PaintShape> paintShapes = whiteboardService.getShapes(whiteboardId, userId, after);
                String json = JsonConverter.toJsonPaintShapes(paintShapes).toString();
                HttpUtils.sendJson(httpExchange, json);
            } catch (JSONException | IllegalStateException exception) {
                HttpUtils.sendBadRequestCode(httpExchange);
            } finally {
                httpExchange.close();
            }
        }
    }

    private class AddShapesHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            log.debug("Start handle request by AddShapesHandler");

            try {
                Map<String, String> queryParams = HttpUtils.getQueryParams(httpExchange.getRequestURI());

                String whiteboardId = HttpUtils.getParam(queryParams, "whiteboard");
                String userId = HttpUtils.getParam(queryParams, "user");

                log.debug("Add shapes to whiteboard [" + whiteboardId + "] by user [" + userId + "]");

                String jsonString = IOUtils.toString(httpExchange.getRequestBody(), CHARSET);
                JSONObject jsonObject = new JSONObject(jsonString);
                List<PaintShape> paintShapes = JsonConverter.fromJsonPaintShapes(jsonObject);
                whiteboardService.addShapes(whiteboardId, userId, paintShapes);

                HttpUtils.sendJson(httpExchange, "{}");
            } catch (JSONException | IllegalStateException exception) {
                HttpUtils.sendBadRequestCode(httpExchange);
            } finally {
                httpExchange.close();
            }
        }
    }
}
