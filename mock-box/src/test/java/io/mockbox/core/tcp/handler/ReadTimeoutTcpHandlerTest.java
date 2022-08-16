package io.mockbox.core.tcp.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import io.mockbox.core.tcp.MockTcpServer;
import io.mockbox.core.tcp.MockTcpServerBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReadTimeoutTcpHandlerTest {
    private static final int TEST_PORT = 10070;
    private static final Duration TEST_TIMEOUT = Duration.ofMillis(500);
    private MockTcpServer mockTcpServer;

    @BeforeEach
    void setUp() {
        mockTcpServer =
                MockTcpServerBuilder.builder()
                        .handler(new ReadTimeoutTcpHandler(TEST_TIMEOUT))
                        .port(TEST_PORT)
                        .buildAndStart();
    }

    @Test
    void testShouldBeThrownExceptionOfReadTimeout() throws IOException {
        Socket socket = new Socket("localhost", TEST_PORT);
        socket.setSoTimeout((int) TEST_TIMEOUT.minusMillis(10).toMillis());

        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        output.write("Hi".getBytes());

        try {
            input.read(new byte[2]);
            fail("It should be not call");
        } catch (SocketTimeoutException e) {
            assertThat(e.getMessage()).isEqualToIgnoringCase("Read timed out");
        } finally {
            socket.close();
        }
    }

    @AfterEach
    void tearDown() {
        mockTcpServer.stop();
    }
}
