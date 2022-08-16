package io.mockbox.core.http.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mockbox.core.error.MockBoxError;
import io.mockbox.core.error.MockBoxException;
import io.mockbox.core.http.HttpMethod;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import java.util.function.BiFunction;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public final class JsonHttpHandler implements HttpHandler {
    private final HttpMethod method;
    private final String uri;
    private final int statusCode;
    private final Object jsonResponse;
    private final ObjectMapper objectMapper;

    public JsonHttpHandler(
            HttpMethod method,
            String uri,
            int statusCode,
            Object jsonResponse,
            ObjectMapper objectMapper) {
        this.method = method;
        this.uri = uri;
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
        this.objectMapper = objectMapper;
    }

    public JsonHttpHandler(HttpMethod method, String uri, int statusCode, Object jsonResponse) {
        this(method, uri, statusCode, jsonResponse, new ObjectMapper());
    }

    public JsonHttpHandler(
            HttpMethod method, String uri, Object jsonResponse, ObjectMapper objectMapper) {
        this(method, uri, 200, jsonResponse, objectMapper);
    }

    public JsonHttpHandler(HttpMethod method, String uri, Object jsonResponse) {
        this(method, uri, 200, jsonResponse, new ObjectMapper());
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> handle() {
        return (request, response) -> {
            try {
                return response.status(statusCode)
                        .header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                        .sendString(Mono.just(objectMapper.writeValueAsString(jsonResponse)));
            } catch (JsonProcessingException e) {
                throw new MockBoxException(MockBoxError.INVALID_JSON_DATA, e);
            }
        };
    }
}
