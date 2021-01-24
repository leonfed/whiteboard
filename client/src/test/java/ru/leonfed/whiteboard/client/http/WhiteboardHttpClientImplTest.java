package ru.leonfed.whiteboard.client.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import ru.leonfed.whiteboard.core.model.JsonConverter;
import ru.leonfed.whiteboard.core.model.ModelGenerators;
import ru.leonfed.whiteboard.core.model.PaintShape;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WhiteboardHttpClientImplTest {

    WhiteboardHttpClientImpl whiteboardHttpClient;

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);

    @Before
    public void initClient() {
        whiteboardHttpClient = new WhiteboardHttpClientImpl("http::/localhost:8000");
        whiteboardHttpClient.setHttpClientSupplier(() -> httpClient);
    }

    private void mockJsonResponse(JSONObject jsonResponse) throws IOException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(httpClient.execute(any())).thenReturn(response);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK"));

        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(jsonResponse.toString().getBytes()));
        when(response.getEntity()).thenReturn(entity);
    }

    private void mockErrorResponse() throws IOException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(httpClient.execute(any())).thenReturn(response);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_BAD_REQUEST, "BAD REQUEST"));
    }

    @Test
    public void createWhiteboard() throws IOException, JSONException {
        String whiteboardId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        JSONObject jsonResponse = new JSONObject().put("whiteboard", whiteboardId).put("user", userId);
        mockJsonResponse(jsonResponse);

        whiteboardHttpClient.createWhiteboard();

        assertThat(whiteboardHttpClient.getWhiteboardId()).isEqualTo(whiteboardId);
        assertThat(whiteboardHttpClient.getUserId()).isEqualTo(userId);
    }

    @Test
    public void joinWhiteboard() throws IOException, JSONException {
        String userId = UUID.randomUUID().toString();

        JSONObject jsonResponse = new JSONObject().put("user", userId);
        mockJsonResponse(jsonResponse);

        whiteboardHttpClient.joinToWhiteboard(UUID.randomUUID().toString());

        assertThat(whiteboardHttpClient.getUserId()).isEqualTo(userId);
    }

    @Test
    public void getShapes() throws IOException, JSONException {
        PaintShape paintLine1 = ModelGenerators.generatePaintLine();
        PaintShape paintLine2 = ModelGenerators.generatePaintLine();

        JSONObject jsonResponse = JsonConverter.toJsonPaintShapes(List.of(paintLine1, paintLine2));
        mockJsonResponse(jsonResponse);

        List<PaintShape> actualPaintShapes = whiteboardHttpClient.getShapes(Instant.MIN);

        assertThat(actualPaintShapes).containsExactlyInAnyOrder(paintLine1, paintLine2);
    }

    @Test(expected = IOException.class)
    public void throwIOExceptionWithErrorResponse() throws JSONException, IOException {
        mockErrorResponse();
        whiteboardHttpClient.postShapes(List.of());
    }
}
