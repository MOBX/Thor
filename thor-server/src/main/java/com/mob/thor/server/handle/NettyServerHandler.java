/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.server.handle;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mob.thor.common.protocol.JSyncPacket;

/**
 * @author zxc Dec 2, 2015 2:17:04 PM
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<JSyncPacket> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private static final ConcurrentHashMap<String, Channel> sessionChannelMap = new ConcurrentHashMap<String, Channel>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSyncPacket msg) throws Exception {
        logger.info("[Server Receipt]" + msg);

        msg.setFlag(1);
        logger.info("[Server send]" + msg);
        ctx.channel().writeAndFlush(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("[Server Client contact]" + ctx.channel().remoteAddress());
        sessionChannelMap.put("ctx.channel().remoteAddress()", ctx.channel());
    }

    public static Collection<Channel> getChannels() {
        return sessionChannelMap.values();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                // logger.info("read idle");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                // logger.info("write idle");
            } else if (event.state() == IdleState.ALL_IDLE) {
                JSyncPacket message = new JSyncPacket();
                message.setPacketType(2);
                ctx.channel().writeAndFlush(message);
                logger.info("[Server 链路空闲！发送心跳!]" + message);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        sessionChannelMap.remove(ctx.channel());
        ctx.close();
    }
}
