package io.mockbox.core.support.logging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LoggerFactoryTest {
    @Test
    void testShouldInstanceOfLogger() {
        Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);
        assertThat(logger).isInstanceOf(Logger.class);
    }
}
