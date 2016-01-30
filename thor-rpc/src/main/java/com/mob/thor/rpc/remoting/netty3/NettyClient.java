package com.mob.thor.rpc.remoting.netty3;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;
import com.mob.thor.rpc.common.utils.NamedThreadFactory;
import com.mob.thor.rpc.common.utils.NetUtils;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.transport.AbstractClient;

public class NettyClient extends AbstractClient {

    private static final Logger         logger         = LoggerFactory.getLogger(NettyClient.class);

    // 因ChannelFactory的关闭有DirectMemory泄露，采用静态化规避
    // https://issues.jboss.org/browse/NETTY-424
    private static final ChannelFactory channelFactory = new NioClientSocketChannelFactory(
                                                                                           Executors.newCachedThreadPool(new NamedThreadFactory(
                                                                                                                                                "NettyClientBoss",
                                                                                                                                                true)),
                                                                                           Executors.newCachedThreadPool(new NamedThreadFactory(
                                                                                                                                                "NettyClientWorker",
                                                                                                                                                true)),
                                                                                           Constants.DEFAULT_IO_THREADS);
    private ClientBootstrap             bootstrap;

    private volatile Channel            channel;                                                                         // volatile,
                                                                                                                          // please
                                                                                                                          // copy
                                                                                                                          // reference
                                                                                                                          // to
                                                                                                                          // use

    public NettyClient(final URL url, final ThorChannelHandler handler) throws RemotingException {
        super(url, wrapChannelHandler(url, handler));
    }

    @Override
    protected void doOpen() throws Throwable {
        NettyHelper.setNettyLoggerFactory();
        bootstrap = new ClientBootstrap(channelFactory);
        // config
        // @see org.jboss.netty.channel.socket.SocketChannelConfig
        bootstrap.setOption("keepAlive", true);
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("connectTimeoutMillis", getTimeout());
        final NettyHandler nettyHandler = new NettyHandler(getUrl(), this);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() {
                NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec(), getUrl(), NettyClient.this);
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", adapter.getDecoder());
                pipeline.addLast("encoder", adapter.getEncoder());
                pipeline.addLast("handler", nettyHandler);
                return pipeline;
            }
        });
    }

    protected void doConnect() throws Throwable {
        long start = System.currentTimeMillis();
        ChannelFuture future = bootstrap.connect(getConnectAddress());
        try {
            boolean ret = future.awaitUninterruptibly(getConnectTimeout(), TimeUnit.MILLISECONDS);

            if (ret && future.isSuccess()) {
                Channel newChannel = future.getChannel();
                newChannel.setInterestOps(Channel.OP_READ_WRITE);
                try {
                    // 关闭旧的连接
                    Channel oldChannel = NettyClient.this.channel; // copy reference
                    if (oldChannel != null) {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("Close old netty channel " + oldChannel + " on create new netty channel "
                                            + newChannel);
                            }
                            oldChannel.close();
                        } finally {
                            NettyChannel.removeChannelIfDisconnected(oldChannel);
                        }
                    }
                } finally {
                    if (NettyClient.this.isClosed()) {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("Close new netty channel " + newChannel + ", because the client closed.");
                            }
                            newChannel.close();
                        } finally {
                            NettyClient.this.channel = null;
                            NettyChannel.removeChannelIfDisconnected(newChannel);
                        }
                    } else {
                        NettyClient.this.channel = newChannel;
                    }
                }
            } else if (future.getCause() != null) {
                throw new RemotingException(this, "client(url: " + getUrl() + ") failed to connect to server "
                                                  + getRemoteAddress() + ", error message is:"
                                                  + future.getCause().getMessage(), future.getCause());
            } else {
                throw new RemotingException(this, "client(url: " + getUrl() + ") failed to connect to server "
                                                  + getRemoteAddress() + " client-side timeout " + getConnectTimeout()
                                                  + "ms (elapsed: " + (System.currentTimeMillis() - start)
                                                  + "ms) from netty client " + NetUtils.getLocalHost());
            }
        } finally {
            if (!isConnected()) {
                future.cancel();
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
        /*
         * try { bootstrap.releaseExternalResources(); } catch (Throwable t) { logger.warn(t.getMessage()); }
         */
    }

    @Override
    protected ThorChannel getChannel() {
        Channel c = channel;
        if (c == null || !c.isConnected()) return null;
        return NettyChannel.getOrAddChannel(c, getUrl(), this);
    }
}
