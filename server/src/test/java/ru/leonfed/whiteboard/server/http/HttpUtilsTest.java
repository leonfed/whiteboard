package ru.leonfed.whiteboard.server.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HttpUtilsTest {

    @Test
    public void getQueryParams() throws URISyntaxException {
        URI uri = new URI("?a=b&c=d");
        Map<String, String> param = HttpUtils.getQueryParams(uri);
        assertThat(param).hasSize(2);
        assertThat(param).containsEntry("a", "b");
        assertThat(param).containsEntry("c", "d");
    }

    @Test
    public void getShapesAfterTimestamp() {
        String name = "name";
        String value = "value";
        HashMap<String, String> params = new HashMap<>();
        params.put(name, value);
        assertThat(HttpUtils.getParam(params, name)).isEqualTo(value);
    }

    @Test(expected = IllegalStateException.class)
    public void getNotExistedParam() {
        HttpUtils.getParam(new HashMap<>(), "param");
    }

    @Test
    public void sendJson() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getResponseHeaders()).thenReturn(mock(Headers.class));
        when(httpExchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        HttpUtils.sendJson(httpExchange, "{}");

        verify(httpExchange).sendResponseHeaders(eq(200), anyLong());
    }

    @Test
    public void sendBadRequestCode() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);

        HttpUtils.sendBadRequestCode(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(400), anyLong());
    }
}
