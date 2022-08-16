package io.mockbox.core.tcp.handler;

import static org.assertj.core.api.Assertions.assertThat;

import io.mockbox.core.tcp.MockTcpServer;
import io.mockbox.core.tcp.MockTcpServerBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EchoTcpHandlerTest {
    private static final int TEST_PORT = 10070;
    private MockTcpServer mockTcpServer;

    @BeforeEach
    void setUp() {
        mockTcpServer =
                MockTcpServerBuilder.builder()
                        .handler(new EchoTcpHandler())
                        .port(TEST_PORT)
                        .buildAndStart();
    }

    @Test
    void testShouldBeReceiveDataFromEchoHandler() throws IOException {
        Socket socket = new Socket("localhost", TEST_PORT);
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();

        output.write("Hi".getBytes());

        byte[] receive = new byte[2];
        input.read(receive);
        socket.close();

        assertThat(new String(receive)).isEqualTo("Hi");
    }

    @AfterEach
    void tearDown() {
        mockTcpServer.stop();
    }
}
