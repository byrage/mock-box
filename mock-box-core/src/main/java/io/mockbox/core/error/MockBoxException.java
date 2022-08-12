package io.mockbox.core.error;

public final class MockBoxException extends RuntimeException {
    public MockBoxException(MockBoxError error, Throwable cause) {
        super(error.getDisplayErrorMessage(), cause);
    }
}
