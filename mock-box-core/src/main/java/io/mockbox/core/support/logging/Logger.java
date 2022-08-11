package io.mockbox.core.support.logging;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class Logger {
    private static final String loggerClassName = Logger.class.getName();

    private final String name;

    private final java.util.logging.Logger javaLogger;

    Logger(String name) {
        this.name = name;
        this.javaLogger = java.util.logging.Logger.getLogger(this.name);
    }

    public void error(Supplier<String> messageSupplier) {
        log(Level.SEVERE, null, messageSupplier);
    }

    public void error(Throwable throwable, Supplier<String> messageSupplier) {
        log(Level.SEVERE, throwable, messageSupplier);
    }

    public void warn(Supplier<String> messageSupplier) {
        log(Level.WARNING, null, messageSupplier);
    }

    public void warn(Throwable throwable, Supplier<String> messageSupplier) {
        log(Level.WARNING, throwable, messageSupplier);
    }

    public void info(Supplier<String> messageSupplier) {
        log(Level.INFO, null, messageSupplier);
    }

    public void info(Throwable throwable, Supplier<String> messageSupplier) {
        log(Level.INFO, throwable, messageSupplier);
    }

    public void config(Supplier<String> messageSupplier) {
        log(Level.CONFIG, null, messageSupplier);
    }

    public void config(Throwable throwable, Supplier<String> messageSupplier) {
        log(Level.CONFIG, throwable, messageSupplier);
    }

    public void debug(Supplier<String> messageSupplier) {
        log(Level.FINE, null, messageSupplier);
    }

    public void debug(Throwable throwable, Supplier<String> messageSupplier) {
        log(Level.FINE, throwable, messageSupplier);
    }

    public void trace(Supplier<String> messageSupplier) {
        log(Level.FINER, null, messageSupplier);
    }

    public void trace(Throwable throwable, Supplier<String> messageSupplier) {
        log(Level.FINER, throwable, messageSupplier);
    }

    private void log(Level level, Throwable throwable, Supplier<String> messageSupplier) {
        boolean loggable = this.javaLogger.isLoggable(level);
        LogRecord logRecord = createLogRecord(level, throwable, nullSafeGet(messageSupplier));
        if (loggable) {
            this.javaLogger.log(logRecord);
        }
    }

    private LogRecord createLogRecord(Level level, Throwable throwable, String message) {
        String sourceClassName = null;
        String sourceMethodName = null;
        boolean found = false;
        for (StackTraceElement element : new Throwable().getStackTrace()) {
            String className = element.getClassName();
            if (!loggerClassName.equals(className) && !found) {
                sourceClassName = className;
                sourceMethodName = element.getMethodName();
                found = true;
            }
        }

        LogRecord logRecord = new LogRecord(level, message);
        logRecord.setLoggerName(this.name);
        logRecord.setThrown(throwable);
        logRecord.setSourceClassName(sourceClassName);
        logRecord.setSourceMethodName(sourceMethodName);
        logRecord.setResourceBundleName(this.javaLogger.getResourceBundleName());
        logRecord.setResourceBundle(this.javaLogger.getResourceBundle());

        return logRecord;
    }

    private static String nullSafeGet(Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }
}
