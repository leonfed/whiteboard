package ru.leonfed.whiteboard.client.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import ru.leonfed.whiteboard.core.model.JsonConverter;
import ru.leonfed.whiteboard.core.model.PaintShape;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class WhiteboardHttpClientImpl implements WhiteboardHttpClient {

    private final String defaultUrl;

    private String whiteboardId;
    private String userId;

    public WhiteboardHttpClientImpl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    @Override
    public void createWhiteboard() throws IOException, JSONException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(defaultUrl + "/whiteboard/create");
            HttpResponse response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());

            //TODO use logging
            System.out.println("Get json: " + json);

            JSONObject jsonObject = new JSONObject(json);
            whiteboardId = jsonObject.getString("whiteboard");
            userId = jsonObject.getString("user");

            //todo use logging
            System.out.println("Create whiteboard: " + whiteboardId + "   User:" + userId);
        }
    }

    @Override
    public void joinToWhiteboard(String whiteboardId) throws JSONException, IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            this.whiteboardId = whiteboardId;

            //todo maybe using smart adding query params
            HttpGet request = new HttpGet(defaultUrl + "/whiteboard/join?whiteboard=" + whiteboardId);
            HttpResponse response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());

            //TODO use logging
            System.out.println("Get json: " + json);

            JSONObject jsonObject = new JSONObject(json);
            userId = jsonObject.getString("user");

            //todo use logging
            System.out.println("Join to whiteboard: " + whiteboardId + "   User:" + userId);
        }
    }

    @Override
    public List<PaintShape> getShapes(Instant after) throws JSONException, IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            //TODO use logging
            System.out.println("Start getting shapes");
            //todo maybe using smart adding query params
            HttpGet request = new HttpGet(defaultUrl +
                    "/shapes/get?whiteboard=" + whiteboardId +
                    "&user=" + userId +
                    "&after=" + after.toString()
            );
            HttpResponse response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());

            //TODO use logging
            System.out.println("Get json: " + json);

            JSONObject jsonObject = new JSONObject(json);
            return JsonConverter.fromJsonPaintShapes(jsonObject);
        }
    }

    @Override
    public void postShapes(List<PaintShape> shapes) throws IOException, JSONException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            //TODO use logging
            System.out.println("Start posting shapes");

            HttpPost request = new HttpPost(defaultUrl +
                    "/shapes/add?whiteboard=" + whiteboardId +
                    "&user=" + userId
            );

            String json = JsonConverter.toJsonPaintShapes(shapes).toString();

            //TODO use logging
            System.out.println("Send json: " + json);

            request.setEntity(new StringEntity(json));
            request.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(request);

            //todo write more smart handing codes
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Response code is not 200");
            }
        }
    }
}
