package io.mockbox.core.tcp;

import io.mockbox.core.tcp.handler.TcpHandler;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

public final class MockTcpServer {
    private DisposableServer server;
    private final int port;
    private final TcpHandler handler;

    MockTcpServer(int port, TcpHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    public void start() {
        this.server = TcpServer.create().port(port).handle(handler.handle()).bindNow();
    }

    public void stop() {
        server.disposeNow();
    }
}
