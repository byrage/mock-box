package io.mockbox.core.http.handler;

import io.mockbox.core.http.HttpMethod;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.time.Duration;
import java.util.function.BiFunction;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public final class ReadTimeoutHttpHandler implements HttpHandler {
    private final HttpMethod method;
    private final String uri;
    private final Duration timeout;

    public ReadTimeoutHttpHandler(HttpMethod method, String uri, Duration timeout) {
        this.method = method;
        this.uri = uri;
        this.timeout = timeout;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> handle() {
        return (request, response) ->
                response.status(HttpResponseStatus.OK)
                        .header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
                        .sendString(Mono.just(request.toString()).delayElement(timeout));
    }
}
