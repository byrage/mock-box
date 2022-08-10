package io.mockbox.core.tcp.handler;

import java.nio.charset.Charset;
import java.util.function.BiFunction;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

public class MessageTcpHandler implements TcpHandler {
    private final byte[] message;

    public MessageTcpHandler(byte[] message) {
        this.message = message;
    }

    public MessageTcpHandler(String message) {
        this(message.getBytes());
    }

    public MessageTcpHandler(String message, Charset charset) {
        this(message.getBytes(charset));
    }

    public BiFunction<NettyInbound, NettyOutbound, Publisher<Void>> handle() {
        return (inbound, outbound) -> outbound.sendByteArray(Mono.just(message));
    }
}
