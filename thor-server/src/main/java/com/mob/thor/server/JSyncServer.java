/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lamfire.json.JSON;
import com.mob.thor.common.cons.JSyncConstant;
import com.mob.thor.common.exception.RemotingException;
import com.mob.thor.common.protocol.*;
import com.mob.thor.common.protocol.JSyncPacket.PacketType;
import com.mob.thor.common.utils.NamedThreadFactory;
import com.mob.thor.server.handle.NettyServerHandler;

/**
 * @author zxc Dec 2, 2015 2:16:53 PM
 */
public class JSyncServer implements JSyncConstant {

    private static final Logger logger      = LoggerFactory.getLogger(JSyncServer.class);
    // private Map<String, AliChannel> channels;
    private Channel             channel;
    private EventLoopGroup      bossGroup;
    private EventLoopGroup      workerGroup;

    private int                 idleTimeout = 600;

    private InetSocketAddress   localAddress;
    private InetSocketAddress   bindAddress;

    public JSyncServer() {

    }

    public static void main(String[] args) throws Exception {
        JSyncServer server = new JSyncServer();
        server.doOpen();
        while (true) {
            Thread.sleep(1000);
            String __ = "{ \"_id\" : \"06f7a619283a1464698149ae8c98436550f1899c\", \"sdkVersion\" : 13, \"carrier\" : 46002,"
                        + " \"screenSize\" : \"480x854\", \"networkType\" : \"wifi\", \"model\" : \"HUAWEI Y511-T00\", \"sysVersion\" : "
                        + "\"17\", \"plat\" : 1, \"factory\" : \"HUAWEI\", \"mac\" : \"f4:dc:f9:45:ea:7c\","
                        + " \"serialNo\" : \"0123456789ABCDEF\", \"imei\" : \"865026023013138\", \"createAt\" : "
                        + "1435680000000, \"androidId\" : \"5fd13a6fd871f69e\" }";
            JSON json = JSON.fromJSONString(__);
            JSyncPacket message = new JSyncPacket(PacketType.MESSAGES, json.toBytes());
            server.send(message);
        }
    }

    protected void doOpen() throws Exception {
        bossGroup = new NioEventLoopGroup(1, (new NamedThreadFactory("NettyServerBoss", true)));
        workerGroup = new NioEventLoopGroup(DEFAULT_IO_THREADS, new NamedThreadFactory("NettyServerWorker", true));

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)//
        .channel(NioServerSocketChannel.class)//
        .childOption(ChannelOption.TCP_NODELAY, false)//
        .childHandler(new ChannelInitializer<Channel>() {

            @Override
            public void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new NettyDecoder());
                pipeline.addLast(new NettyEncoder());
                pipeline.addLast(new IdleStateHandler(0, 0, serverChannelMaxIdleTimeSeconds));
                pipeline.addLast(new NettyServerHandler());
            }
        });

        ChannelFuture channelFuture = bootstrap.bind(getBindAddress()).sync();
        channelFuture.awaitUninterruptibly();
        channel = channelFuture.channel();
        // channel.closeFuture().sync();
    }

    protected void doClose() throws Throwable {
        if (channel != null) {
            channel.close().syncUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    public void send(JSyncPacket message) throws RemotingException {
        Collection<Channel> channels = NettyServerHandler.getChannels();
        if (channels.isEmpty()) {
            return;
        }
        for (Channel channel : channels) {
            channel.writeAndFlush(message);
        }
        logger.info("[server channels send] " + message);
    }

    public boolean isBound() {
        return channel.isRegistered();
    }

    private SocketAddress getBindAddress() {
        String host = "0.0.0.0";
        int port = 9096;
        return new InetSocketAddress(host, port);
    }
}
