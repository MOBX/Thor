package com.mob.thor.rpc.remoting.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

import com.mob.thor.rpc.remoting.api.buffer.ChannelBuffer;
import com.mob.thor.rpc.remoting.api.buffer.ChannelBufferFactory;

public class NettyBackedChannelBufferFactory implements ChannelBufferFactory {

    private static final NettyBackedChannelBufferFactory INSTANCE = new NettyBackedChannelBufferFactory();

    public static ChannelBufferFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public ChannelBuffer getBuffer(int capacity) {
        return new NettyBackedChannelBuffer(ByteBufAllocator.DEFAULT.buffer(capacity));
    }

    @Override
    public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(length);
        buffer.writeBytes(array, offset, length);
        return new NettyBackedChannelBuffer(buffer);
    }

    @Override
    public ChannelBuffer getBuffer(ByteBuffer nioBuffer) {
        return new NettyBackedChannelBuffer(Unpooled.wrappedBuffer(nioBuffer));
    }
}
