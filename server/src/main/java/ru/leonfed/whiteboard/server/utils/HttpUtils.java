package ru.leonfed.whiteboard.server.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtils {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final int STATUS_OK = 200;

    public static void sendJson(HttpExchange httpExchange, String json) throws IOException {
        Headers headers = httpExchange.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        byte[] rawResponseBody = json.getBytes(CHARSET);
        httpExchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
        httpExchange.getResponseBody().write(rawResponseBody);
    }

    public static Map<String, String> getQueryParams(URI uri) {
        return URLEncodedUtils.parse(uri, CHARSET)
                .stream()
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
    }
}
