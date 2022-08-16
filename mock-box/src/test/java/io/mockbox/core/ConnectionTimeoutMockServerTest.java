package io.mockbox.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.mockbox.core.ConnectionTimeoutMockServer.ConnectionTimeoutMockServerBuilder;
import io.mockbox.core.error.MockBoxError;
import io.mockbox.core.error.MockBoxException;
import org.junit.jupiter.api.Test;

class ConnectionTimeoutMockServerTest {
    @Test
    void testShouldBeThrownExceptionOfServerOperationFailedWhenAlreadyStart() {
        ConnectionTimeoutMockServer mockTcpServer =
                ConnectionTimeoutMockServerBuilder.builder().buildAndStart();
        try {
            mockTcpServer.start();
        } catch (MockBoxException e) {
            assertThat(e.getError()).isEqualTo(MockBoxError.SERVER_OPERATION_FAILED);
        }
    }

    @Test
    void testShouldBeThrownExceptionOfServerOperationFailedWhenNotStarted() {
        ConnectionTimeoutMockServer mockTcpServer =
                ConnectionTimeoutMockServerBuilder.builder().build();
        try {
            mockTcpServer.stop();
        } catch (MockBoxException e) {
            assertThat(e.getError()).isEqualTo(MockBoxError.SERVER_OPERATION_FAILED);
        }
    }
}
