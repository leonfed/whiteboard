package ru.leonfed.whiteboard.client.http;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import ru.leonfed.whiteboard.core.logging.Logger;
import ru.leonfed.whiteboard.core.logging.LoggerFactory;
import ru.leonfed.whiteboard.core.model.JsonConverter;
import ru.leonfed.whiteboard.core.model.PaintShape;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class WhiteboardHttpClientImpl implements WhiteboardHttpClient {

    static final Logger log = LoggerFactory.logger(WhiteboardHttpClientImpl.class);

    private final String createWhiteboardUrl;
    private final String joinWhiteboardUrl;
    private final String getShapesUrl;
    private final String postShapesUrl;

    private String whiteboardId;
    private String userId;

    public WhiteboardHttpClientImpl(String mainUrl) {
        createWhiteboardUrl = mainUrl + "/whiteboard/create";
        joinWhiteboardUrl = mainUrl + "/whiteboard/join";
        getShapesUrl = mainUrl + "/shapes/get";
        postShapesUrl = mainUrl + "/shapes/add";
    }

    private String sendRequest(HttpRequestBase request) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String requestId = RandomStringUtils.random(8, true, true);
            log.debug("[" + requestId + "] Send request: " + request.toString());

            HttpResponse response = client.execute(request);
            log.debug("[" + requestId + "] Get response: " + response.getStatusLine().toString());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Response code is not OK");
            }

            return EntityUtils.toString(response.getEntity());
        }
    }

    @Override
    public void createWhiteboard() throws IOException, JSONException {
        log.debug("Creating whiteboard");

        HttpGet request = new HttpGet(createWhiteboardUrl);
        String response = sendRequest(request);
        JSONObject jsonObject = new JSONObject(response);

        whiteboardId = jsonObject.getString("whiteboard");
        userId = jsonObject.getString("user");

        log.info("Create whiteboard: " + whiteboardId);
        log.info("Get user id: " + userId);
    }

    @Override
    public void joinToWhiteboard(String whiteboardId) throws IOException, JSONException {
        log.debug("Join to whiteboard");

        this.whiteboardId = whiteboardId;
        HttpGet request = new HttpGet(joinWhiteboardUrl + "?whiteboard=" + whiteboardId);

        String response = sendRequest(request);
        JSONObject jsonObject = new JSONObject(response);

        userId = jsonObject.getString("user");

        log.info("Join to whiteboard: " + whiteboardId);
        log.info("Get user id: " + userId);
    }

    @Override
    public List<PaintShape> getShapes(Instant after) throws IOException, JSONException {
        log.debug("Get shapes");

        HttpGet request = new HttpGet(getShapesUrl +
                "?whiteboard=" + whiteboardId +
                "&user=" + userId +
                "&after=" + after.toString()
        );

        String response = sendRequest(request);
        JSONObject jsonObject = new JSONObject(response);
        return JsonConverter.fromJsonPaintShapes(jsonObject);
    }

    @Override
    public void postShapes(List<PaintShape> shapes) throws IOException, JSONException {
        log.debug("Post shapes");

        HttpPost request = new HttpPost(postShapesUrl + "?whiteboard=" + whiteboardId + "&user=" + userId);
        String json = JsonConverter.toJsonPaintShapes(shapes).toString();
        request.setEntity(new StringEntity(json));
        request.setHeader("Content-type", "application/json");

        sendRequest(request);
    }
}
