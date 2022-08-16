package io.mockbox.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import io.mockbox.core.ConnectionTimeoutMockServer.ConnectionTimeoutMockServerBuilder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectionTimeoutMockServerTcpTest {
    private static final int TEST_PORT = 10070;
    private ConnectionTimeoutMockServer mockTcpServer;

    @BeforeEach
    void setUp() {
        mockTcpServer =
                ConnectionTimeoutMockServerBuilder.builder().port(TEST_PORT).buildAndStart();
    }

    @Test
    void testShouldBeThrownExceptionOfConnectionTimeout() throws IOException {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", TEST_PORT), 500);
            fail("It should be not call");
        } catch (SocketTimeoutException e) {
            assertThat(e.getMessage()).isEqualTo("connect timed out");
        }
    }

    @AfterEach
    void tearDown() {
        mockTcpServer.stop();
    }
}
