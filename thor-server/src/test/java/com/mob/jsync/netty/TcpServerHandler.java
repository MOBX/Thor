/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.jsync.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zxc Dec 2, 2015 2:17:04 PM
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    // 接收到新的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        // MyNettyDecoder将接收到的数据由ByteBuf转为String
        String message = (String) msg;
        System.out.println("channelRead:" + message);

        // MyNettyEncoder将write的字符串添加了一个小字节序Header并转为字节码
        ctx.writeAndFlush("收到");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
