package io.mockbox.core.tcp.handler;

import java.time.Duration;
import java.util.function.BiFunction;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

public final class ReadTimeoutTcpHandler implements TcpHandler {
    private final Duration timeout;

    public ReadTimeoutTcpHandler(Duration timeout) {
        this.timeout = timeout;
    }

    public BiFunction<NettyInbound, NettyOutbound, Publisher<Void>> handle() {
        return (inbound, outbound) ->
                inbound.receive()
                        .asString()
                        .flatMap(
                                (message) ->
                                        outbound.sendString(
                                                Mono.just(message).delayElement(timeout)));
    }
}
