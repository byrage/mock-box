package io.mockbox.core.http.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import io.mockbox.core.http.HttpMethod;
import io.mockbox.core.http.MockHttpServer;
import io.mockbox.core.http.MockHttpServerBuilder;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.netty.http.client.HttpClient;

class ReadTimeoutHttpHandlerTest {
    private static final int TEST_PORT = 10080;
    private static final Duration TEST_TIMEOUT = Duration.ofMillis(500);
    private MockHttpServer mockHttpServer;

    @BeforeEach
    void setUp() {
        mockHttpServer =
                MockHttpServerBuilder.builder()
                        .addHandler(
                                new ReadTimeoutHttpHandler(HttpMethod.GET, "/hello", TEST_TIMEOUT))
                        .port(TEST_PORT)
                        .buildAndStart();
    }

    @Test
    void testShouldBeThrownExceptionOfReadTimeout() {
        try {
            HttpClient.create()
                    .doOnConnected(
                            conn ->
                                    conn.addHandlerFirst(
                                            new ReadTimeoutHandler(
                                                    TEST_TIMEOUT.minusMillis(10).toMillis(),
                                                    TimeUnit.MILLISECONDS)))
                    .get()
                    .uri("http://localhost:10080/hello")
                    .response()
                    .block();
            fail();
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(ReadTimeoutException.class);
        }
    }

    @AfterEach
    void tearDown() {
        mockHttpServer.stop();
    }
}
