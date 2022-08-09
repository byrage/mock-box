# Mock-Box
Mock-Box is a lightweight and powerful mock library that supports testing.

## How to use

### MockTcpServer
```java
    @BeforeEach
    void setUp() {
        mockTcpServer = MockTcpServerBuilder.builder()
                        .handler(new EchoTcpHandler()) OR .handler(new MessageTcpHandler("Bye"))
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
