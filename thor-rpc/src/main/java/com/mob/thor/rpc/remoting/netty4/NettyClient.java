package com.mob.thor.rpc.remoting.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.SystemPropertyUtil;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;
import com.mob.thor.rpc.common.utils.NamedThreadFactory;
import com.mob.thor.rpc.common.utils.NetUtils;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.transport.AbstractClient;

public class NettyClient extends AbstractClient {

    private static final Logger logger    = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup      workerGroup;
    private Bootstrap           bootstrap = null;
    private volatile Channel    channel;

    public NettyClient(final URL url, final ThorChannelHandler handler) throws RemotingException {
        super(url, wrapChannelHandler(url, handler));
    }

    @Override
    protected void doOpen() throws Throwable {
        String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
        NettyHelper.setNettyLoggerFactory();

        workerGroup = new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS,
                                            new NamedThreadFactory("NettyClientTCPWorker", true));

        bootstrap = new Bootstrap();
        if (name == null || !name.startsWith("linux")) {
            workerGroup = new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS,
                                                new NamedThreadFactory("NettyClientTCPWorker", true));
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
        } else {
            workerGroup = new EpollEventLoopGroup(Constants.DEFAULT_IO_THREADS,
                                                  new NamedThreadFactory("NettyClientTCPWorker", true));
            bootstrap.group(workerGroup);
            bootstrap.channel(EpollSocketChannel.class);
        }
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                                                                                                          getTimeout()).option(ChannelOption.ALLOCATOR,
                                                                                                                               PooledByteBufAllocator.DEFAULT).option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK,
                                                                                                                                                                      32 * 1024).option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK,
                                                                                                                                                                                        8 * 1024);

        final NettyHandler nettyHandler = new NettyHandler(getUrl(), this);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel channel) {
                NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec(), getUrl(), NettyClient.this);
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("decoder", adapter.getDecoder());
                pipeline.addLast("encoder", adapter.getEncoder());
                pipeline.addLast("handler", nettyHandler);
            }
        });
    }

    protected void doConnect() throws Throwable {
        long start = System.currentTimeMillis();
        ChannelFuture future = bootstrap.connect(getConnectAddress());
        try {
            boolean ret = future.awaitUninterruptibly(getConnectTimeout(), TimeUnit.MILLISECONDS);

            if (ret && future.isSuccess()) {
                Channel newChannel = future.channel();
                // newChannel.config().setAutoRead(true);
                try {
                    // 关闭旧的连接
                    Channel oldChannel = NettyClient.this.channel; // copy
                                                                   // reference
                    if (oldChannel != null) {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("Close old netty channel " + oldChannel + " on create new netty channel "
                                            + newChannel);
                            }
                            oldChannel.close().syncUninterruptibly();
                        } finally {
                            NettyChannel.removeChannelIfDisconnected(oldChannel);
                        }
                    }
                } finally {
                    if (!newChannel.isActive()) {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("Close new netty channel " + newChannel + ", because the client closed.");
                            }
                            newChannel.close().syncUninterruptibly();
                        } finally {
                            NettyClient.this.channel = null;
                            NettyChannel.removeChannelIfDisconnected(newChannel);
                        }
                    } else {
                        NettyClient.this.channel = newChannel;
                    }
                }
            } else if (future.cause() != null) {
                throw new RemotingException(this, "client(url: " + getUrl() + ") failed to connect to server "
                                                  + getRemoteAddress() + ", error message is:"
                                                  + future.cause().getMessage(), future.cause());
            } else {
                throw new RemotingException(this, "client(url: " + getUrl() + ") failed to connect to server "
                                                  + getRemoteAddress() + " client-side timeout " + getConnectTimeout()
                                                  + "ms (elapsed: " + (System.currentTimeMillis() - start)
                                                  + "ms) from netty client " + NetUtils.getLocalHost()
                                                  + " using thor version ");
            }
        } finally {
            if (!isConnected()) {
                future.cancel(true);
            }
        }
    }

    @Override
    protected void doDisConnect() throws Throwable {
        try {
            NettyChannel.removeChannelIfDisconnected(channel);
        } catch (Throwable t) {
            logger.warn(t.getMessage());
        }
    }

    @Override
    protected void doClose() throws Throwable {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
            workerGroup = null;
        }
    }

    @Override
    protected com.mob.thor.rpc.remoting.api.ThorChannel getChannel() {
        Channel c = channel;
        if (c == null || !c.isActive()) return null;
        return NettyChannel.getOrAddChannel(c, getUrl(), this);
    }
}
