package ru.leonfed.whiteboard.server.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import ru.leonfed.whiteboard.core.logging.Logger;
import ru.leonfed.whiteboard.core.logging.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtils {

    static final Logger log = LoggerFactory.logger("http-access-log");

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final int STATUS_OK = 200;
    private static final int STATUS_BAD_REQUEST = 400;

    private static void logRequest(HttpExchange httpExchange, String requestId) {
        log.debug("[" + requestId + "] Get request: " +
                httpExchange.getRequestMethod() + " " +
                httpExchange.getRequestURI()
        );
    }

    private static void logResponse(HttpExchange httpExchange, String requestId) {
        log.debug("[" + requestId + "] Send response: " + httpExchange.getResponseCode());
    }

    public static void sendJson(HttpExchange httpExchange, String json) throws IOException {
        String requestId = RandomStringUtils.random(8, true, true);
        logRequest(httpExchange, requestId);

        Headers headers = httpExchange.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        byte[] rawResponseBody = json.getBytes(CHARSET);
        httpExchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
        httpExchange.getResponseBody().write(rawResponseBody);

        logResponse(httpExchange, requestId);
    }

    public static void sendBadRequestCode(HttpExchange httpExchange) throws IOException {
        String requestId = RandomStringUtils.random(8, true, true);
        logRequest(httpExchange, requestId);

        httpExchange.sendResponseHeaders(STATUS_BAD_REQUEST, -1);

        logResponse(httpExchange, requestId);
    }

    public static Map<String, String> getQueryParams(URI uri) {
        return URLEncodedUtils.parse(uri, CHARSET)
                .stream()
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
    }

    public static String getParam(Map<String, String> param, String name) throws IllegalStateException {
        if (!param.containsKey(name)) {
            throw new IllegalStateException("Parameter [" + name + "] is absent");
        }
        return param.get(name);
    }
}
