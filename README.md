# Mock-Box

Mock-Box is a lightweight and powerful mock library that supports testing.

[![Maven Central](https://img.shields.io/maven-central/v/io.mock-box/mock-box.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.mock-box%22%20AND%20a:%22mock-box%22)
[![CI](https://github.com/mock-box/mock-box/actions/workflows/ci.yml/badge.svg)](https://github.com/mock-box/mock-box/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/mock-box/mock-box/branch/main/graph/badge.svg?token=WDD5JM0OOM)](https://codecov.io/gh/mock-box/mock-box)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)
[![Twitter](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Ftwitter.com%2FProject_MockBox)](https://twitter.com/Project_MockBox)

## How to use

### MockTcpServer

```java
@BeforeEach
void setUp() {
    mockTcpServer = MockTcpServerBuilder.builder()
        .handler(new EchoTcpHandler()) Or .handler(new MessageTcpHandler("Bye"))
        .port(PORT_NO)
        .buildAndStart();
}

@Test
void test() {...}

@AfterEach
void tearDown() {
    mockTcpServer.stop();
}
```

### MockHttpServer

```java
@BeforeEach
void setUp() {
    mockHttpServer = MockHttpServerBuilder.builder()
        .addHandler(new HttpJsonHandler(HttpMethod.GET, "/hello", responseObject))
        .port(PORT_NO)
        .buildAndStart();
}

@Test
void test() {...}

@AfterEach
void tearDown() {
    mockHttpServer.stop();
}
```
