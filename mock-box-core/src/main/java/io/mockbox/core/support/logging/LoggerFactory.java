package io.mockbox.core.support.logging;

public final class LoggerFactory {
    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz.getName());
    }
}
