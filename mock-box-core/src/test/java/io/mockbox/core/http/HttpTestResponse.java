package io.mockbox.core.http;

public class HttpTestResponse {
    private final String name;
    private final int level;

    public HttpTestResponse(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}
