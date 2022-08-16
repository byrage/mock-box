package io.mockbox.core.tcp;

import io.mockbox.core.tcp.handler.TcpHandler;

public final class MockTcpServerBuilder {
    private int port = 10070;
    private TcpHandler handler;

    private MockTcpServerBuilder() {}

    public static MockTcpServerBuilder builder() {
        return new MockTcpServerBuilder();
    }

    public MockTcpServerBuilder port(int port) {
        this.port = port;
        return this;
    }

    public MockTcpServerBuilder handler(TcpHandler handler) {
        this.handler = handler;
        return this;
    }

    public MockTcpServer build() {
        return new MockTcpServer(port, handler);
    }

    public MockTcpServer buildAndStart() {
        MockTcpServer server = build();
        server.start();
        return server;
    }
}
