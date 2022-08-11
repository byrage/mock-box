package io.mockbox.core.http;

import io.mockbox.core.error.MockBoxError;
import io.mockbox.core.http.handler.HttpHandler;
import io.mockbox.core.support.logging.Logger;
import io.mockbox.core.support.logging.LoggerFactory;
import java.util.List;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class MockHttpServer {
    private static final Logger logger = LoggerFactory.getLogger(MockHttpServer.class);

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
                                        HttpMethod method = handler.getMethod();
                                        if (method == HttpMethod.GET) {
                                            routes.get(handler.getUri(), handler.handle());
                                        } else if (method == HttpMethod.POST) {
                                            routes.post(handler.getUri(), handler.handle());
                                        } else if (method == HttpMethod.PUT) {
                                            routes.put(handler.getUri(), handler.handle());
                                        } else if (method == HttpMethod.PATCH) {
                                            routes.route(
                                                    HttpPatchMethodPredicate.patch(
                                                            handler.getUri()),
                                                    handler.handle());
                                        } else if (method == HttpMethod.DELETE) {
                                            routes.delete(handler.getUri(), handler.handle());
                                        } else {
                                            logger.warn(
                                                    MockBoxError.NOT_SUPPORT_MODE
                                                            ::getDisplayErrorMessage);
                                        }
                                    }
                                })
                        .bindNow();
    }

    public void stop() {
        server.disposeNow();
    }
}
