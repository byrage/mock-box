package io.mockbox.core.http;

import io.mockbox.core.http.handler.HttpHandler;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.List;
import java.util.function.BiFunction;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public class MockHttpServer {
    private DisposableServer server;
    private final int port;
    private final List<HttpHandler> handlers;

    MockHttpServer(int port, List<HttpHandler> handlers) {
        this.port = port;
        this.handlers = handlers;
    }

    public void start() {
        this.server =
                HttpServer.create()
                        .port(port)
                        .route(
                                routes -> {
                                    for (HttpHandler handler : handlers) {
                                        switch (handler.getMethod()) {
                                            case GET:
                                                routes.get(handler.getUri(), handler.handle());
                                                break;
                                            case POST:
                                                routes.post(handler.getUri(), handler.handle());
                                                break;
                                            case PUT:
                                                routes.put(handler.getUri(), handler.handle());
                                                break;
                                            case PATCH:
                                                routes.route(
                                                        HttpPatchMethodPredicate.patch(
                                                                handler.getUri()),
                                                        handler.handle());
                                                break;
                                            case DELETE:
                                                routes.delete(handler.getUri(), handler.handle());
                                                break;
                                        }
                                    }
                                })
                        .bindNow();
    }

    private static BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> route() {
        return (request, response) ->
                response.status(HttpResponseStatus.OK)
                        .header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                        .sendString(Mono.just("Hello World! Get"));
    }

    public void stop() {
        server.disposeNow();
    }
}
