package io.mockbox.core.error;

public enum MockBoxError {
    NOT_SUPPORT_MODE("M0001", "Not support mode");

    private final String code;
    private final String message;

    MockBoxError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getDisplayErrorMessage() {
        return "[" + code + "] " + message;
    }

    public String getDisplayErrorMessage(String appendMessage) {
        return getDisplayErrorMessage() + " :: " + appendMessage;
    }
}
