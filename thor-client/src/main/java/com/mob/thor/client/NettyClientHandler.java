/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lamfire.json.JSON;
import com.mob.thor.common.protocol.JSyncPacket;

/**
 * @author zxc Dec 8, 2015 6:41:49 PM
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<JSyncPacket> {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSyncPacket msg) throws Exception {
        logger.info("[Client Receipt] " + msg);
        byte[] body = msg.getBody();
        if (body == null) {
            return;
        }
        JSON json = JSON.fromBytes(body);
        logger.info("[Client Receipt Body] " + json);
    }
}
