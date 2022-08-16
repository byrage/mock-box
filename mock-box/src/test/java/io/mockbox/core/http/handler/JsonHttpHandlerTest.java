package io.mockbox.core.http.handler;

import static org.assertj.core.api.Assertions.assertThat;

import io.mockbox.core.helper.mock.MockObjectMapper;
import io.mockbox.core.http.HttpMethod;
import io.mockbox.core.http.HttpTestResponse;
import io.mockbox.core.http.MockHttpServer;
import io.mockbox.core.http.MockHttpServerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

class JsonHttpHandlerTest {
    private static final int TEST_PORT = 10080;
    private MockHttpServer mockHttpServer;

    @BeforeEach
    void setUp() {
        mockHttpServer =
                MockHttpServerBuilder.builder()
                        .addHandler(
                                new JsonHttpHandler(
                                        HttpMethod.GET,
                                        "/hello",
                                        new HttpTestResponse("gemini", 100)))
                        .addHandler(
                                new JsonHttpHandler(
                                        HttpMethod.POST,
                                        "/hello",
                                        200,
                                        new HttpTestResponse("poemini", 10)))
                        .addHandler(
                                new JsonHttpHandler(
                                        HttpMethod.PUT,
                                        "/hello",
                                        200,
                                        new HttpTestResponse("puemini", 20)))
                        .addHandler(
                                new JsonHttpHandler(
                                        HttpMethod.PATCH,
                                        "/hello",
                                        200,
                                        new HttpTestResponse("paemini", 30)))
                        .addHandler(
                                new JsonHttpHandler(
                                        HttpMethod.DELETE,
                                        "/hello",
                                        200,
                                        new HttpTestResponse("deemini", 40)))
                        .addHandler(
                                new JsonHttpHandler(
                                        HttpMethod.GET,
                                        "/helloCustomObjectMapper",
                                        new HttpTestResponse("omnemini", 50),
                                        new MockObjectMapper()))
                        .addHandler(
                                new JsonHttpHandler(
                                        HttpMethod.GET,
                                        "/helloCustomObjectMapperWithStatus",
                                        200,
                                        new HttpTestResponse("omsemini", 60),
                                        new MockObjectMapper()))
                        .addHandler(
                                new JsonHttpHandler(
                                        null,
                                        "/helloNullMethod",
                                        200,
                                        new HttpTestResponse("omsemini", 60),
                                        new MockObjectMapper()))
                        .port(TEST_PORT)
                        .buildAndStart();
    }

    @Test
    void testShouldBeReceiveDataFromJsonHandler() {
        HttpClient client = HttpClient.create();

        String resultGet =
                client.get()
                        .uri("http://localhost:10080/hello")
                        .responseContent()
                        .aggregate()
                        .asString()
                        .block();

        String resultPost =
                client.post()
                        .uri("http://localhost:10080/hello")
                        .send(ByteBufFlux.fromString(Mono.just("hello")))
                        .responseContent()
                        .aggregate()
                        .asString()
                        .block();

        String resultPut =
                client.put()
                        .uri("http://localhost:10080/hello")
                        .send(ByteBufFlux.fromString(Mono.just("hello")))
                        .responseContent()
                        .aggregate()
                        .asString()
                        .block();

        String resultPatch =
                client.patch()
                        .uri("http://localhost:10080/hello")
                        .send(ByteBufFlux.fromString(Mono.just("hello")))
                        .responseContent()
                        .aggregate()
                        .asString()
                        .block();

        String resultDelete =
                client.delete()
                        .uri("http://localhost:10080/hello")
                        .responseContent()
                        .aggregate()
                        .asString()
                        .block();

        assertThat(resultGet).isEqualTo("{\"name\":\"gemini\",\"level\":100}");
        assertThat(resultPost).isEqualTo("{\"name\":\"poemini\",\"level\":10}");
        assertThat(resultPut).isEqualTo("{\"name\":\"puemini\",\"level\":20}");
        assertThat(resultPatch).isEqualTo("{\"name\":\"paemini\",\"level\":30}");
        assertThat(resultDelete).isEqualTo("{\"name\":\"deemini\",\"level\":40}");
    }

    @Test
    void testShouldBeReceiveNullFromJsonHandlerWithMockObjectMapper() {
        HttpClient client = HttpClient.create();

        String resultGetCustomObjectMapper =
                client.get()
                        .uri("http://localhost:10080/helloCustomObjectMapper")
                        .responseContent()
                        .aggregate()
                        .asString()
                        .block();

        String resultGetCustomObjectMapperWithStatus =
                client.get()
                        .uri("http://localhost:10080/helloCustomObjectMapperWithStatus")
                        .responseContent()
                        .aggregate()
                        .asString()
                        .block();

        assertThat(resultGetCustomObjectMapper).isEqualTo(null);
        assertThat(resultGetCustomObjectMapperWithStatus).isEqualTo(null);
    }

    @Test
    void testShouldBeReceiveNullFromJsonHandlerWithNullMethod() {
        HttpClient client = HttpClient.create();

        String resultGetCustomObjectMapper =
                client.get()
                        .uri("http://localhost:10080/helloNullMethod")
                        .responseContent()
                        .aggregate()
                        .asString()
                        .block();

        assertThat(resultGetCustomObjectMapper).isEqualTo(null);
    }

    @AfterEach
    void tearDown() {
        mockHttpServer.stop();
    }
}
