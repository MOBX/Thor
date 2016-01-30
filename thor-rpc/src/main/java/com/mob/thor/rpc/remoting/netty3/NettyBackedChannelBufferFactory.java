package com.mob.thor.rpc.remoting.netty3;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffers;

import com.mob.thor.rpc.remoting.api.buffer.ChannelBuffer;
import com.mob.thor.rpc.remoting.api.buffer.ChannelBufferFactory;

public class NettyBackedChannelBufferFactory implements ChannelBufferFactory {

    private static final NettyBackedChannelBufferFactory INSTANCE = new NettyBackedChannelBufferFactory();

    public static ChannelBufferFactory getInstance() {
        return INSTANCE;
    }

    public ChannelBuffer getBuffer(int capacity) {
        return new NettyBackedChannelBuffer(ChannelBuffers.dynamicBuffer(capacity));
    }

    public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
        org.jboss.netty.buffer.ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(length);
        buffer.writeBytes(array, offset, length);
        return new NettyBackedChannelBuffer(buffer);
    }

    public ChannelBuffer getBuffer(ByteBuffer nioBuffer) {
        return new NettyBackedChannelBuffer(ChannelBuffers.wrappedBuffer(nioBuffer));
    }
}
