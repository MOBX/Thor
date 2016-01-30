package com.mob.thor.rpc.remoting.api.exchange.support.header;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.*;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.Version;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;
import com.mob.thor.rpc.common.utils.NamedThreadFactory;
import com.mob.thor.rpc.remoting.api.*;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeChannel;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeServer;
import com.mob.thor.rpc.remoting.api.exchange.Request;
import com.mob.thor.rpc.remoting.api.exchange.support.DefaultFuture;

public class HeaderExchangeServer implements ExchangeServer {

    protected final Logger                 logger    = LoggerFactory.getLogger(getClass());

    private final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1,
                                                                                        new NamedThreadFactory(
                                                                                                               "thor-remoting-server-heartbeat",
                                                                                                               true));

    // 心跳定时器
    private ScheduledFuture<?>             heatbeatTimer;

    // 心跳超时，毫秒。缺省0，不会执行心跳。
    private int                            heartbeat;

    private int                            heartbeatTimeout;

    private final Server                   server;

    private volatile boolean               closed    = false;

    public HeaderExchangeServer(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("server == null");
        }
        this.server = server;
        this.heartbeat = server.getUrl().getParameter(Constants.HEARTBEAT_KEY, 0);
        this.heartbeatTimeout = server.getUrl().getParameter(Constants.HEARTBEAT_TIMEOUT_KEY, heartbeat * 3);
        if (heartbeatTimeout < heartbeat * 2) {
            throw new IllegalStateException("heartbeatTimeout < heartbeatInterval * 2");
        }
        startHeatbeatTimer();
    }

    public Server getServer() {
        return server;
    }

    public boolean isClosed() {
        return server.isClosed();
    }

    private boolean isRunning() {
        Collection<ThorChannel> channels = getChannels();
        for (ThorChannel channel : channels) {
            if (DefaultFuture.hasFuture(channel)) {
                return true;
            }
        }
        return false;
    }

    public void close() {
        doClose();
        server.close();
    }

    public void close(final int timeout) {
        if (timeout > 0) {
            final long max = (long) timeout;
            final long start = System.currentTimeMillis();
            if (getUrl().getParameter(Constants.CHANNEL_SEND_READONLYEVENT_KEY, false)) {
                sendChannelReadOnlyEvent();
            }
            while (HeaderExchangeServer.this.isRunning() && System.currentTimeMillis() - start < max) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        doClose();
        server.close(timeout);
    }

    private void sendChannelReadOnlyEvent() {
        Request request = new Request();
        request.setEvent(Request.READONLY_EVENT);
        request.setTwoWay(false);
        request.setVersion(Version.getVersion());

        Collection<ThorChannel> channels = getChannels();
        for (ThorChannel channel : channels) {
            try {
                if (channel.isConnected()) channel.send(request,
                                                        getUrl().getParameter(Constants.CHANNEL_READONLYEVENT_SENT_KEY,
                                                                              true));
            } catch (RemotingException e) {
                logger.warn("send connot write messge error.", e);
            }
        }
    }

    private void doClose() {
        if (closed) {
            return;
        }
        closed = true;
        stopHeartbeatTimer();
        try {
            scheduled.shutdown();
        } catch (Throwable t) {
            logger.warn(t.getMessage(), t);
        }
    }

    public Collection<ExchangeChannel> getExchangeChannels() {
        Collection<ExchangeChannel> exchangeChannels = new ArrayList<ExchangeChannel>();
        Collection<ThorChannel> channels = server.getChannels();
        if (channels != null && channels.size() > 0) {
            for (ThorChannel channel : channels) {
                exchangeChannels.add(HeaderExchangeChannel.getOrAddChannel(channel));
            }
        }
        return exchangeChannels;
    }

    public ExchangeChannel getExchangeChannel(InetSocketAddress remoteAddress) {
        ThorChannel channel = server.getChannel(remoteAddress);
        return HeaderExchangeChannel.getOrAddChannel(channel);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Collection<ThorChannel> getChannels() {
        return (Collection) getExchangeChannels();
    }

    public ThorChannel getChannel(InetSocketAddress remoteAddress) {
        return getExchangeChannel(remoteAddress);
    }

    public boolean isBound() {
        return server.isBound();
    }

    public InetSocketAddress getLocalAddress() {
        return server.getLocalAddress();
    }

    public URL getUrl() {
        return server.getUrl();
    }

    public ThorChannelHandler getChannelHandler() {
        return server.getChannelHandler();
    }

    public void reset(URL url) {
        server.reset(url);
        try {
            if (url.hasParameter(Constants.HEARTBEAT_KEY) || url.hasParameter(Constants.HEARTBEAT_TIMEOUT_KEY)) {
                int h = url.getParameter(Constants.HEARTBEAT_KEY, heartbeat);
                int t = url.getParameter(Constants.HEARTBEAT_TIMEOUT_KEY, h * 3);
                if (t < h * 2) {
                    throw new IllegalStateException("heartbeatTimeout < heartbeatInterval * 2");
                }
                if (h != heartbeat || t != heartbeatTimeout) {
                    heartbeat = h;
                    heartbeatTimeout = t;
                    startHeatbeatTimer();
                }
            }
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
    }

    public void send(Object message) throws RemotingException {
        if (closed) {
            throw new RemotingException(this.getLocalAddress(), null, "Failed to send message " + message
                                                                      + ", cause: The server " + getLocalAddress()
                                                                      + " is closed!");
        }
        server.send(message);
    }

    public void send(Object message, boolean sent) throws RemotingException {
        if (closed) {
            throw new RemotingException(this.getLocalAddress(), null, "Failed to send message " + message
                                                                      + ", cause: The server " + getLocalAddress()
                                                                      + " is closed!");
        }
        server.send(message, sent);
    }

    private void startHeatbeatTimer() {
        stopHeartbeatTimer();
        if (heartbeat > 0) {
            heatbeatTimer = scheduled.scheduleWithFixedDelay(new HeartBeatTask(new HeartBeatTask.ChannelProvider() {

                public Collection<ThorChannel> getChannels() {
                    return Collections.unmodifiableCollection(HeaderExchangeServer.this.getChannels());
                }
            }, heartbeat, heartbeatTimeout), heartbeat, heartbeat, TimeUnit.MILLISECONDS);
        }
    }

    private void stopHeartbeatTimer() {
        try {
            ScheduledFuture<?> timer = heatbeatTimer;
            if (timer != null && !timer.isCancelled()) {
                timer.cancel(true);
            }
        } catch (Throwable t) {
            logger.warn(t.getMessage(), t);
        } finally {
            heatbeatTimer = null;
        }
    }
}
