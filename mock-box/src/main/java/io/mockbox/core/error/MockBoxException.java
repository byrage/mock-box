package io.mockbox.core.error;

public final class MockBoxException extends RuntimeException {
    private final MockBoxError error;

    public MockBoxException(MockBoxError error, Throwable cause) {
        super(error.getDisplayErrorMessage(), cause);
        this.error = error;
    }

    public MockBoxError getError() {
        return error;
    }
}
