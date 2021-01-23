package ru.leonfed.whiteboard.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import ru.leonfed.whiteboard.core.model.JsonConverter;
import ru.leonfed.whiteboard.core.model.PaintShape;
import ru.leonfed.whiteboard.server.service.WhiteboardService;
import ru.leonfed.whiteboard.server.utils.HttpUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Server {
    private static final int BACKLOG = 10;

    private static final String WHITEBOARD_PARAM = "whiteboard";
    private static final String USER_PARAM = "user";
    private static final String AFTER_PARAM = "after";

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final WhiteboardService whiteboardService;
    private final HttpServer server;

    public Server(String hostname, int port, WhiteboardService whiteboardService) throws IOException {
        this.whiteboardService = whiteboardService;
        this.server = HttpServer.create(new InetSocketAddress(hostname, port), BACKLOG);
        server.createContext("/whiteboard/create", new CreateWhiteboardHandler());
        server.createContext("/whiteboard/join", new JoinWhiteboardHandler());
        server.createContext("/shapes/get", new GetShapesHandler());
        server.createContext("/shapes/add", new AddShapesHandler());
    }

    void start() {
        server.start();
    }

    private class CreateWhiteboardHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                String whiteboardId = whiteboardService.createWhiteboard();
                String userId = whiteboardService.joinToWhiteboard(whiteboardId);

                //TODO use logging
                System.out.println("Create whiteboard: " + whiteboardId + "  User: " + userId);

                String json = new JSONObject().put("whiteboard", whiteboardId).put("user", userId).toString();

                //TODO use logging
                System.out.println("Send json: " + json);

                HttpUtils.sendJson(httpExchange, json);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    private class JoinWhiteboardHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                Map<String, String> queryParams = HttpUtils.getQueryParams(httpExchange.getRequestURI());

                //TODO handle if whiteboard param is absent. Send 400 code as response
                String whiteboardId = queryParams.getOrDefault(WHITEBOARD_PARAM, "");

                //TODO handle if whiteboardId is incorrect. Send 400 code as response
                String userId = whiteboardService.joinToWhiteboard(whiteboardId);

                //TODO use logging
                System.out.println("Join to whiteboard: " + whiteboardId + "  User: " + userId);

                String json = new JSONObject().put("user", userId).toString();

                //TODO use logging
                System.out.println("Send json: " + json);

                HttpUtils.sendJson(httpExchange, json);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    private class GetShapesHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) {
            //TODO use logging
            System.out.println("Start GetShapesHandler");

            try {
                Map<String, String> queryParams = HttpUtils.getQueryParams(httpExchange.getRequestURI());

                //TODO handle if whiteboard/user param is absent. Send 400 code as response
                String whiteboardId = queryParams.getOrDefault(WHITEBOARD_PARAM, "");
                String userId = queryParams.getOrDefault(USER_PARAM, "");
                Instant after = Instant.parse(queryParams.getOrDefault(AFTER_PARAM, ""));

                //TODO use logging
                System.out.println("Get shapes of whiteboard: " + whiteboardId + "  User: " + userId);

                List<PaintShape> paintShapes = whiteboardService.getShapes(whiteboardId, userId, after);
                String json = JsonConverter.toJsonPaintShapes(paintShapes).toString();

                //TODO use logging
                System.out.println("Send json: " + json);

                HttpUtils.sendJson(httpExchange, json);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    private class AddShapesHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) {
            //TODO use logging
            System.out.println("Start AddShapesHandler");

            try {
                Map<String, String> queryParams = HttpUtils.getQueryParams(httpExchange.getRequestURI());

                //TODO handle if whiteboard/user param is absent. Send 400 code as response
                String whiteboardId = queryParams.getOrDefault(WHITEBOARD_PARAM, "");
                String userId = queryParams.getOrDefault(USER_PARAM, "");

                //todo check that this whiteboard contains this user (in WhiteboardDao).

                //TODO use logging
                System.out.println("Add shapes to whiteboard: " + whiteboardId + "  User: " + userId);

                String jsonString = IOUtils.toString(httpExchange.getRequestBody(), CHARSET);

                //TODO use logging
                System.out.println("Get json: " + jsonString);

                JSONObject jsonObject = new JSONObject(jsonString);
                List<PaintShape> paintShapes = JsonConverter.fromJsonPaintShapes(jsonObject);
                whiteboardService.addShapes(whiteboardId, userId, paintShapes);

                //TODO maybe using not 'sendJson' method
                HttpUtils.sendJson(httpExchange, "");

                //TODO use logging
                System.out.println("Send empty json");
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }
}
