package io.mockbox.core.helper.mock;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MockJsonProcessingException extends JsonProcessingException {
    protected MockJsonProcessingException(String msg) {
        super(msg);
    }
}
