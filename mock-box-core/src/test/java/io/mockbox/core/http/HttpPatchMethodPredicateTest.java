package io.mockbox.core.http;

import static org.assertj.core.api.Assertions.assertThat;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.multipart.HttpData;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.netty.ByteBufFlux;
import reactor.netty.Connection;
import reactor.netty.http.server.HttpServerFormDecoderProvider.Builder;
import reactor.netty.http.server.HttpServerRequest;

class HttpPatchMethodPredicateTest {

    @Test
    void testApply() {
        HttpPatchMethodPredicate predicate =
                new HttpPatchMethodPredicate("/matched/{test}", null, HttpMethod.GET);

        assertThat(predicate.apply("/matched/{test}")).containsEntry("test", "{test}");
        assertThat(predicate.apply("/not-matched")).isNull();
    }

    @Test
    void testTest() {
        HttpPatchMethodPredicate predicate =
                new HttpPatchMethodPredicate("/matched/{test}", null, HttpMethod.GET);

        assertThat(predicate.test(key(null, HttpMethod.GET, "/matched/{test}"))).isTrue();
        assertThat(predicate.test(key(HttpVersion.HTTP_1_1, HttpMethod.GET, "/matched/{test}")))
                .isTrue();
        assertThat(predicate.test(key(HttpVersion.HTTP_1_1, HttpMethod.GET, "/not-matched")))
                .isFalse();
    }

    @Test
    void testTestWithHttpVersion() {
        HttpPatchMethodPredicate predicate =
                new HttpPatchMethodPredicate(
                        "/matched/{test}", HttpVersion.HTTP_1_1, HttpMethod.GET);

        assertThat(predicate.test(key(null, HttpMethod.GET, "/matched/{test}"))).isFalse();
        assertThat(predicate.test(key(HttpVersion.HTTP_1_1, HttpMethod.GET, "/matched/{test}")))
                .isTrue();
        assertThat(predicate.test(key(HttpVersion.HTTP_1_1, HttpMethod.GET, "/not-matched")))
                .isFalse();
    }

    private MockHttpServerRequest key(HttpVersion version, HttpMethod get, String uri) {
        return new MockHttpServerRequest(version, get, uri);
    }

    private static class MockHttpServerRequest implements HttpServerRequest {
        private final HttpVersion version;
        private final HttpMethod method;
        private final String uri;

        public MockHttpServerRequest(HttpVersion version, HttpMethod method, String uri) {
            this.version = version;
            this.method = method;
            this.uri = uri;
        }

        @Override
        public ByteBufFlux receive() {
            return null;
        }

        @Override
        public Flux<?> receiveObject() {
            return null;
        }

        @Override
        public HttpServerRequest withConnection(Consumer<? super Connection> withConnection) {
            return null;
        }

        @Override
        public String param(CharSequence key) {
            return null;
        }

        @Override
        public Map<String, String> params() {
            return null;
        }

        @Override
        public HttpServerRequest paramsResolver(
                Function<? super String, Map<String, String>> paramsResolver) {
            return null;
        }

        @Override
        public boolean isFormUrlencoded() {
            return false;
        }

        @Override
        public boolean isMultipart() {
            return false;
        }

        @Override
        public Flux<HttpData> receiveForm() {
            return null;
        }

        @Override
        public Flux<HttpData> receiveForm(Consumer<Builder> formDecoderBuilder) {
            return null;
        }

        @Override
        public InetSocketAddress hostAddress() {
            return null;
        }

        @Override
        public InetSocketAddress remoteAddress() {
            return null;
        }

        @Override
        public HttpHeaders requestHeaders() {
            return null;
        }

        @Override
        public String scheme() {
            return null;
        }

        @Override
        public Map<CharSequence, List<Cookie>> allCookies() {
            return null;
        }

        @Override
        public Map<CharSequence, Set<Cookie>> cookies() {
            return null;
        }

        @Override
        public String fullPath() {
            return null;
        }

        @Override
        public String requestId() {
            return null;
        }

        @Override
        public boolean isKeepAlive() {
            return false;
        }

        @Override
        public boolean isWebsocket() {
            return false;
        }

        @Override
        public HttpMethod method() {
            return method;
        }

        @Override
        public String uri() {
            return uri;
        }

        @Override
        public HttpVersion version() {
            return version;
        }
    }
}
