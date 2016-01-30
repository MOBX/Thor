/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zxc Dec 8, 2015 6:31:08 PM
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger logger           = LoggerFactory.getLogger(NettyDecoder.class);
    private static final int    FRAME_MAX_LENGTH = 8388608;

    public NettyDecoder() {
        super(FRAME_MAX_LENGTH, 0, 4, 0, 4);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;
            }
            ByteBuffer byteBuffer = frame.nioBuffer();
            return JSyncPacket.decode(byteBuffer);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (null != frame) {
                frame.release();
            }
        }
        return null;
    }
}
