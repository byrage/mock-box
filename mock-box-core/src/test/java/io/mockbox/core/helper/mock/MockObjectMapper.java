package io.mockbox.core.helper.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MockObjectMapper extends ObjectMapper {
    public String writeValueAsString(Object value) throws JsonProcessingException {
        throw new MockJsonProcessingException("Invalid data");
    }
}
