/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mob.thor.common.cons.JSyncConstant;
import com.mob.thor.common.exception.RemotingException;
import com.mob.thor.common.protocol.*;
import com.mob.thor.common.utils.NamedThreadFactory;

/**
 * @author zxc Dec 8, 2015 1:33:10 PM
 */
public class JSyncClient implements JSyncConstant {

    private static final Logger logger = LoggerFactory.getLogger(JSyncClient.class);

    private Bootstrap           bootstrap;
    private volatile Channel    channel;
    private static final EventLoopGroup WORKER_GROUP = new NioEventLoopGroup(DEFAULT_IO_THREADS,new NamedThreadFactory("NettyClientTCPWorker",true));


    public JSyncClient() {

    }

    public static void main(String[] args) throws Exception {
        JSyncClient client = new JSyncClient();
        client.doOpen();
    }

    protected void doOpen() throws Exception {
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(WORKER_GROUP);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new NettyDecoder());
                pipeline.addLast(new NettyEncoder());
                pipeline.addLast(new IdleStateHandler(0, 0, clientChannelMaxIdleTimeSeconds));
                pipeline.addLast(new NettyClientHandler());
            }
        });

        ChannelFuture channelFuture = bootstrap.connect(getBindAddress()).sync();
        channelFuture.awaitUninterruptibly();
        channel = channelFuture.channel();
        channel.closeFuture().sync();
    }

    public void send(JSyncPacket message) throws RemotingException {
        logger.info("[client send] " + message);
        channel.writeAndFlush(message);
    }

    private SocketAddress getBindAddress() {
        String host = "192.168.180.118";
        int port = 9096;
        return new InetSocketAddress(host, port);
    }
}
