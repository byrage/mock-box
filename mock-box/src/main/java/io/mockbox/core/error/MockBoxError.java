package io.mockbox.core.error;

public enum MockBoxError {
    NOT_SUPPORT_MODE("M0001", "Not support mode"),
    INVALID_JSON_DATA("M0002", "Invalid json data"),
    SERVER_OPERATION_FAILED("M0003", "Server operation failed");

    private final String code;
    private final String message;

    MockBoxError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getDisplayErrorMessage() {
        return "[" + code + "] " + message;
    }
}
