package io.mockbox.core.tcp;

import static org.assertj.core.api.Assertions.assertThat;

import io.mockbox.core.tcp.handler.MessageTcpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageTcpHandlerTest {
    private static final int TEST_PORT = 10070;
    private MockTcpServer mockTcpServer;

    @BeforeEach
    void setUp() {
        mockTcpServer =
                MockTcpServerBuilder.builder()
                        .handler(new MessageTcpHandler("Bye"))
                        .port(TEST_PORT)
                        .buildAndStart();
    }

    @Test
    void testShouldBeReceiveDataFromEchoHandler() throws IOException {
        Socket socket = new Socket("localhost", TEST_PORT);
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();

        output.write("Hi".getBytes());

        byte[] receive = new byte[3];
        input.read(receive);
        socket.close();

        assertThat(new String(receive)).isEqualTo("Bye");
    }

    @AfterEach
    void tearDown() {
        mockTcpServer.stop();
    }
}
