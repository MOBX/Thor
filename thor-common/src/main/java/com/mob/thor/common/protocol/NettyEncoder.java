/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zxc Dec 8, 2015 6:32:48 PM
 */
public class NettyEncoder extends MessageToByteEncoder<JSyncPacket> {

    private static final Logger logger = LoggerFactory.getLogger(NettyEncoder.class);

    @Override
    public void encode(ChannelHandlerContext ctx, JSyncPacket packet, ByteBuf out) throws Exception {
        try {
            ByteBuffer header = packet.encodeHeader();
            out.writeBytes(header);
            byte[] body = packet.getBody();
            if (body != null) {
                out.writeBytes(body);
            }
        } catch (Exception e) {
            logger.error("encode exception, ", e);
            if (packet != null) {
                logger.error(packet.toString());
            }
        }
    }
}
