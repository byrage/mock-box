package io.mockbox.core.error;

public class MockBoxException extends RuntimeException {
    public MockBoxException(MockBoxError error) {
        super(error.getDisplayErrorMessage());
    }

    public MockBoxException(MockBoxError error, Throwable cause) {
        super(error.getDisplayErrorMessage(), cause);
    }

    public MockBoxException(MockBoxError error, String appendMessage) {
        super(error.getDisplayErrorMessage(appendMessage));
    }

    public MockBoxException(MockBoxError error, String appendMessage, Throwable cause) {
        super(error.getDisplayErrorMessage(appendMessage), cause);
    }
}
