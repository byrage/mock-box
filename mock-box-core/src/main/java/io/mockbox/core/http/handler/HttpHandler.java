package io.mockbox.core.http.handler;

import io.mockbox.core.http.HttpMethod;
import java.util.function.BiFunction;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public interface HttpHandler {
    BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> handle();

    HttpMethod getMethod();

    String getUri();
}
