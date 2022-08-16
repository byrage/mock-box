package io.mockbox.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import io.mockbox.core.ConnectionTimeoutMockServer.ConnectionTimeoutMockServerBuilder;
import io.netty.channel.ChannelOption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.netty.http.client.HttpClient;

class ConnectionTimeoutMockServerHttpTest {
    private static final int TEST_PORT = 10060;
    private ConnectionTimeoutMockServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = ConnectionTimeoutMockServerBuilder.builder().port(TEST_PORT).buildAndStart();
    }

    @Test
    void testShouldBeThrownExceptionOfConnectionTimeout() {
        try {
            HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500)
                    .get()
                    .uri("http://localhost:10060/hello")
                    .response()
                    .block();
            fail("It should be not call");
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("connection timed out");
        }
    }

    @AfterEach
    void tearDown() {
        mockServer.stop();
    }
}
