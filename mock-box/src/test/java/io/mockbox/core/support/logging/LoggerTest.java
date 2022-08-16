package io.mockbox.core.support.logging;

import org.junit.jupiter.api.Test;

class LoggerTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);
    private static final Logger logger2 = LoggerFactory.getLogger(String.class);

    @Test
    void testShouldBeLoggedDebug() {
        logger.debug(() -> "DEBUG-LOG");
        logger.debug(new RuntimeException("DEBUG-LOG"), () -> "DEBUG-LOG");
    }

    @Test
    void testShouldBeLoggedInfo() {
        logger.info(() -> "INFO-LOG");
        logger.info(new RuntimeException("INFO-LOG"), () -> "INFO-LOG");
    }

    @Test
    void testShouldBeLoggedError() {
        logger.error(() -> "ERROR-LOG");
        logger.error(new RuntimeException("ERROR-LOG"), () -> "ERROR-LOG");
    }

    @Test
    void testShouldBeLoggedWarn() {
        logger.warn(() -> "WARN-LOG");
        logger.warn(new RuntimeException("WARN-LOG"), () -> "WARN-LOG");
    }

    @Test
    void testShouldBeLoggedTrace() {
        logger.trace(() -> "TRACE-LOG");
        logger.trace(new RuntimeException("TRACE-LOG"), () -> "TRACE-LOG");
    }

    @Test
    void testShouldBeLoggedConfig() {
        logger.config(() -> "CONFIG-LOG");
        logger.config(new RuntimeException("CONFIG-LOG"), () -> "CONFIG-LOG");
    }

    @Test
    void testShouldBeLoggedInfoOfNullMessage() {
        logger.info(null);
    }
}
