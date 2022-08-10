package io.mockbox.core.tcp.handler;

import java.util.function.BiFunction;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

public final class EchoTcpHandler implements TcpHandler {
    public BiFunction<NettyInbound, NettyOutbound, Publisher<Void>> handle() {
        return (inbound, outbound) ->
                inbound.receive()
                        .asString()
                        .flatMap((message) -> outbound.sendString(Mono.just(message)));
    }
}
