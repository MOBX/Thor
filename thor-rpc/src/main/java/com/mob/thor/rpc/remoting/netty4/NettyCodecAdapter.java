package com.mob.thor.rpc.remoting.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.util.List;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.Codec2;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.buffer.DynamicChannelBuffer;

final class NettyCodecAdapter {

    private final ChannelHandler     encoder = new InternalEncoder();

    private final ChannelHandler     decoder = new InternalDecoder();

    private final Codec2             codec;

    private final URL                url;

    private final int                bufferSize;

    private final ThorChannelHandler handler;

    public NettyCodecAdapter(Codec2 codec, URL url, ThorChannelHandler handler) {
        this.codec = codec;
        this.url = url;
        this.handler = handler;
        int b = url.getPositiveParameter(Constants.BUFFER_KEY, Constants.DEFAULT_BUFFER_SIZE);
        this.bufferSize = b >= Constants.MIN_BUFFER_SIZE && b <= Constants.MAX_BUFFER_SIZE ? b : Constants.DEFAULT_BUFFER_SIZE;
    }

    public ChannelHandler getEncoder() {
        return encoder;
    }

    public ChannelHandler getDecoder() {
        return decoder;
    }

    @ChannelHandler.Sharable
    private class InternalEncoder extends MessageToMessageEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
            com.mob.thor.rpc.remoting.api.buffer.ChannelBuffer buffer = com.mob.thor.rpc.remoting.api.buffer.ChannelBuffers.dynamicBuffer(1024);
            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
            try {
                codec.encode(channel, buffer, msg);
                if (buffer.readableBytes() > 0) {
                    out.add(ctx.alloc().buffer(buffer.readableBytes()).writeBytes(buffer.toByteBuffer()));
                }
            } finally {
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }
        }
    }

    private class InternalDecoder extends SimpleChannelInboundHandler<ByteBuf> {

        private com.mob.thor.rpc.remoting.api.buffer.ChannelBuffer buffer = com.mob.thor.rpc.remoting.api.buffer.ChannelBuffers.EMPTY_BUFFER;

        @Override
        public void channelRead0(ChannelHandlerContext ctx, ByteBuf input) throws Exception {
            int readable = input.readableBytes();
            if (readable <= 0) {
                return;
            }

            com.mob.thor.rpc.remoting.api.buffer.ChannelBuffer message;
            if (buffer.readable()) {
                if (buffer instanceof DynamicChannelBuffer) {
                    buffer.writeBytes(input.nioBuffer());
                    message = buffer;
                } else {
                    int size = buffer.readableBytes() + input.readableBytes();
                    message = com.mob.thor.rpc.remoting.api.buffer.ChannelBuffers.dynamicBuffer(size > bufferSize ? size : bufferSize);
                    message.writeBytes(buffer, buffer.readableBytes());
                    message.writeBytes(input.nioBuffer());
                }
            } else {
                message = com.mob.thor.rpc.remoting.api.buffer.ChannelBuffers.dynamicBuffer(input.readableBytes(),
                                                                                            NettyBackedChannelBufferFactory.getInstance());
                message.writeBytes(input.nioBuffer());
            }

            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
            Object msg;
            int saveReaderIndex;

            try {
                // decode object.
                do {
                    saveReaderIndex = message.readerIndex();
                    try {
                        msg = codec.decode(channel, message);
                    } catch (IOException e) {
                        buffer = com.mob.thor.rpc.remoting.api.buffer.ChannelBuffers.EMPTY_BUFFER;
                        throw e;
                    }
                    if (msg == Codec2.DecodeResult.NEED_MORE_INPUT) {
                        message.readerIndex(saveReaderIndex);
                        break;
                    } else {
                        if (saveReaderIndex == message.readerIndex()) {
                            buffer = com.mob.thor.rpc.remoting.api.buffer.ChannelBuffers.EMPTY_BUFFER;
                            throw new IOException("Decode without read data.");
                        }
                        if (msg != null) {
                            ctx.fireChannelRead(msg);
                        }
                    }
                } while (message.readable());
            } finally {
                if (message.readable()) {
                    message.discardReadBytes();
                    buffer = message;
                } else {
                    buffer = com.mob.thor.rpc.remoting.api.buffer.ChannelBuffers.EMPTY_BUFFER;
                }
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.fireExceptionCaught(cause);
        }
    }
}
