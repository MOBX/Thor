package com.mob.thor.rpc.remoting.api.buffer;

import java.nio.ByteBuffer;

public class HeapChannelBufferFactory implements ChannelBufferFactory {

    private static final HeapChannelBufferFactory INSTANCE = new HeapChannelBufferFactory();

    public static ChannelBufferFactory getInstance() {
        return INSTANCE;
    }

    public HeapChannelBufferFactory() {
        super();
    }

    public ChannelBuffer getBuffer(int capacity) {
        return ChannelBuffers.buffer(capacity);
    }

    public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
        return ChannelBuffers.wrappedBuffer(array, offset, length);
    }

    public ChannelBuffer getBuffer(ByteBuffer nioBuffer) {
        if (nioBuffer.hasArray()) {
            return ChannelBuffers.wrappedBuffer(nioBuffer);
        }

        ChannelBuffer buf = getBuffer(nioBuffer.remaining());
        int pos = nioBuffer.position();
        buf.writeBytes(nioBuffer);
        nioBuffer.position(pos);
        return buf;
    }

}
