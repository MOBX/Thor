package com.mob.thor.rpc.remoting.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.SystemPropertyUtil;

import java.net.InetSocketAddress;
import java.util.*;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;
import com.mob.thor.rpc.common.utils.ExecutorUtil;
import com.mob.thor.rpc.common.utils.NetUtils;
import com.mob.thor.rpc.remoting.api.*;
import com.mob.thor.rpc.remoting.api.transport.AbstractServer;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.ChannelHandlers;

public class NettyServer extends AbstractServer implements Server {

    private static final Logger      logger = LoggerFactory.getLogger(NettyServer.class);

    private Map<String, ThorChannel> channels;                                           // <ip:port, channel>
    private io.netty.channel.Channel channel;

    private EventLoopGroup           bossGroup;
    private EventLoopGroup           workerGroup;
    private ServerBootstrap          bootstrap;

    public NettyServer(URL url, ThorChannelHandler handler) throws RemotingException {
        super(url, ChannelHandlers.wrap(handler, ExecutorUtil.setThreadName(url, SERVER_THREAD_POOL_NAME)));
    }

    @Override
    protected void doOpen() throws Throwable {
        NettyHelper.setNettyLoggerFactory();
        int threads = getUrl().getPositiveParameter(Constants.IO_THREADS_KEY, Constants.DEFAULT_IO_THREADS);
        String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
        bootstrap = new ServerBootstrap();

        if (!name.startsWith("linux")) {
            bossGroup = new NioEventLoopGroup(threads, new NettyThreadFactory(null, "NettyServerBoss", true));
            workerGroup = new NioEventLoopGroup(threads, new NettyThreadFactory(null, "NettyServerWorker", true));
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
        } else {
            bossGroup = new EpollEventLoopGroup(threads, new NettyThreadFactory(null, "NettyServerBoss", true));
            workerGroup = new EpollEventLoopGroup(threads, new NettyThreadFactory(null, "NettyServerWorker", true));
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(EpollServerSocketChannel.class);
        }
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, getTimeout());
        bootstrap.childOption(ChannelOption.SO_RCVBUF, 100 * 1024);
        bootstrap.childOption(ChannelOption.SO_SNDBUF, 100 * 1024);
        // bootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
        bootstrap.childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024);
        bootstrap.childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024);

        // 选择内存分配模型
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        final NettyHandler nettyHandler = new NettyHandler(getUrl(), this);
        channels = nettyHandler.getChannels();
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel channel) {
                NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec(), getUrl(), NettyServer.this);
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("decoder", adapter.getDecoder());
                pipeline.addLast("encoder", adapter.getEncoder());
                pipeline.addLast("handler", nettyHandler);
            }
        });

        ChannelFuture future = bootstrap.bind(getBindAddress());
        future.awaitUninterruptibly();
        channel = future.channel();
    }

    @Override
    protected void doClose() throws Throwable {
        try {
            if (channel != null) {
                // unbind.
                channel.closeFuture();
                channel.close();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            Collection<com.mob.thor.rpc.remoting.api.ThorChannel> channels = getChannels();
            if (channels != null && channels.size() > 0) {
                for (com.mob.thor.rpc.remoting.api.ThorChannel channel : channels) {
                    try {
                        channel.close();
                    } catch (Throwable e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }

        try {
            // and then shutdown the thread pools
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
            if (bootstrap != null) {
                bootstrap = null;
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }

        try {
            if (channels != null) {
                channels.clear();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public Collection<ThorChannel> getChannels() {
        Collection<ThorChannel> chs = new HashSet<ThorChannel>();
        for (ThorChannel channel : this.channels.values()) {
            if (channel.isConnected()) {
                chs.add(channel);
            } else {
                channels.remove(NetUtils.toAddressString(channel.getRemoteAddress()));
            }
        }
        return chs;
    }

    public ThorChannel getChannel(InetSocketAddress remoteAddress) {
        return channels.get(NetUtils.toAddressString(remoteAddress));
    }

    public boolean isBound() {
        return channel.isRegistered();
    }
}
