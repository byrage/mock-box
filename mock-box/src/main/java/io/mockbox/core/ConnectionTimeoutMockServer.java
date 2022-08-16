package io.mockbox.core;

import io.mockbox.core.error.MockBoxError;
import io.mockbox.core.error.MockBoxException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public final class ConnectionTimeoutMockServer {
    private ServerSocket serverSocket;
    private Socket socket;
    private final int port;

    ConnectionTimeoutMockServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port, 1);
            while (true) {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress("localhost", port), 100);
                } catch (SocketTimeoutException e) {
                    break;
                }
            }
        } catch (Exception e) {
            throw new MockBoxException(MockBoxError.SERVER_OPERATION_FAILED, e);
        }
    }

    public void stop() {
        try {
            serverSocket.close();
            socket.close();
        } catch (Exception e) {
            throw new MockBoxException(MockBoxError.SERVER_OPERATION_FAILED, e);
        }
    }

    public static final class ConnectionTimeoutMockServerBuilder {
        private int port = 10060;

        private ConnectionTimeoutMockServerBuilder() {}

        public static ConnectionTimeoutMockServerBuilder builder() {
            return new ConnectionTimeoutMockServerBuilder();
        }

        public ConnectionTimeoutMockServerBuilder port(int port) {
            this.port = port;
            return this;
        }

        public ConnectionTimeoutMockServer build() {
            return new ConnectionTimeoutMockServer(port);
        }

        public ConnectionTimeoutMockServer buildAndStart() {
            ConnectionTimeoutMockServer server = build();
            server.start();
            return server;
        }
    }
}
