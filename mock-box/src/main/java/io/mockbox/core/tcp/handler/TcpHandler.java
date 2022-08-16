package io.mockbox.core.tcp.handler;

import java.util.function.BiFunction;
import org.reactivestreams.Publisher;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

public interface TcpHandler {
    BiFunction<NettyInbound, NettyOutbound, Publisher<Void>> handle();
}
