package io.mockbox.core.http;

import io.mockbox.core.http.handler.HttpHandler;
import java.util.ArrayList;
import java.util.List;

public final class MockHttpServerBuilder {
    private int port = 10080;
    private List<HttpHandler> handlers = new ArrayList<>();

    private MockHttpServerBuilder() {}

    public static MockHttpServerBuilder builder() {
        return new MockHttpServerBuilder();
    }

    public MockHttpServerBuilder port(int port) {
        this.port = port;
        return this;
    }

    public MockHttpServerBuilder addHandler(HttpHandler handler) {
        this.handlers.add(handler);
        return this;
    }

    public MockHttpServer build() {
        return new MockHttpServer(port, handlers);
    }

    public MockHttpServer buildAndStart() {
        MockHttpServer server = build();
        server.start();
        return server;
    }
}
