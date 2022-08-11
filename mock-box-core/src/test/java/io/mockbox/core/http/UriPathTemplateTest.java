package io.mockbox.core.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import io.mockbox.core.http.HttpPatchMethodPredicate.UriPathTemplate;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.jupiter.api.Test;

public class UriPathTemplateTest {
    @Test
    void patternShouldMatchPathWithOnlyLetters() {
        UriPathTemplate uriPathTemplate = new UriPathTemplate("/test/{order}");
        assertThat(uriPathTemplate.match("/test/1").get("order")).isEqualTo("1");
    }

    @Test
    void patternShouldMatchPathWithDots() {
        UriPathTemplate uriPathTemplate = new UriPathTemplate("/test/{order}");
        assertThat(uriPathTemplate.match("/test/2.0").get("order")).isEqualTo("2.0");
    }

    @Test
    void staticPatternShouldMatchPathWithQueryParams() {
        UriPathTemplate uriPathTemplate = new UriPathTemplate("/test/3");
        assertThat(uriPathTemplate.matches("/test/3?q=reactor")).isTrue();
    }

    @Test
    void parameterizedPatternShouldMatchPathWithQueryParams() {
        UriPathTemplate uriPathTemplate = new UriPathTemplate("/test/{order}");
        assertThat(uriPathTemplate.match("/test/3?q=reactor").get("order")).isEqualTo("3");
    }

    @Test
    void staticPathShouldBeMatched() {
        UriPathTemplate template = new UriPathTemplate("/comments");
        assertThat(template.matches("/comments")).isTrue();
        assertThat(template.match("/comments").entrySet()).isEmpty();
    }

    @Test
    void staticPathWithDotShouldBeMatched() {
        UriPathTemplate template = new UriPathTemplate("/1.0/comments");
        assertThat(template.matches("/1.0/comments")).isTrue();
        assertThat(template.match("/1.0/comments").entrySet()).isEmpty();
    }

    @Test
    void parametrizedPathShouldBeMatched() {
        UriPathTemplate template = new UriPathTemplate("/comments/{id}");
        assertThat(template.matches("/comments/1")).isTrue();
        assertThat(template.match("/comments/1"))
                .hasEntrySatisfying("id", s -> assertThat(s).isEqualTo("1"));
    }

    @Test
    void parametrizedPathWithStaticSuffixShouldBeMatched() {
        UriPathTemplate template = new UriPathTemplate("/comments/{id}/author");
        assertThat(template.matches("/comments/1/author")).isTrue();
        assertThat(template.match("/comments/1/author"))
                .hasEntrySatisfying("id", s -> assertThat(s).isEqualTo("1"));
    }

    @Test
    void parametrizedPathWithMultipleParametersShouldBeMatched() {
        UriPathTemplate template = new UriPathTemplate("/{collection}/{id}");
        assertThat(template.matches("/comments/1")).isTrue();
        assertThat(template.match("/comments/1"))
                .hasEntrySatisfying("id", s -> assertThat(s).isEqualTo("1"));
        assertThat(template.match("/comments/1"))
                .hasEntrySatisfying("collection", s -> assertThat(s).isEqualTo("comments"));
    }

    @Test
    void pathWithDotShouldBeMatched() {
        UriPathTemplate template = new UriPathTemplate("/tags/{tag}");
        assertThat(template.matches("/tags/v1.0.0")).isTrue();
        assertThat(template.match("/tags/v1.0.0"))
                .hasEntrySatisfying("tag", s -> assertThat(s).isEqualTo("v1.0.0"));
    }

    @Test
    void pathVariableShouldNotMatchTrailingSegments() {
        UriPathTemplate template = new UriPathTemplate("/tags/{tag}/commits");
        assertThat(template.matches("/tags/v1.0.0")).isFalse();
        assertThat(template.match("/tags/v1.0.0").entrySet()).isEmpty();
    }

    @Test
    void testShouldNotEmptyUri() {
        try {
            new HttpPatchMethodPredicate(
                    "", HttpVersion.HTTP_1_1, io.netty.handler.codec.http.HttpMethod.GET);
            fail("it should be not called");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Unable to parse url []");
        }
    }

    @Test
    void testShouldBeSupportedUri() {
        assertThat(getUriTemplate("http://localhost:8080/matched")).isTrue();
        assertThat(getUriTemplate("localhost:8080")).isTrue();
        assertThat(getUriTemplate("/matched/{test}/**")).isTrue();
        assertThat(getUriTemplate("/matched/{test}**")).isTrue();
    }

    private static boolean getUriTemplate(String uri) {
        return new UriPathTemplate(uri).matches(uri);
    }
}
