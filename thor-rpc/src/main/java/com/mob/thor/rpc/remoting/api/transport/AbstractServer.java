package com.mob.thor.rpc.remoting.api.transport;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;
import com.mob.thor.rpc.common.utils.ExecutorUtil;
import com.mob.thor.rpc.common.utils.NetUtils;
import com.mob.thor.rpc.remoting.api.*;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.WrappedChannelHandler;

public abstract class AbstractServer extends AbstractEndpoint implements Server {

    private static final Logger   logger                  = LoggerFactory.getLogger(AbstractServer.class);

    private InetSocketAddress     localAddress;

    private InetSocketAddress     bindAddress;

    private int                   accepts;

    private int                   idleTimeout             = 600;                                          // 600
                                                                                                           // seconds

    protected static final String SERVER_THREAD_POOL_NAME = "ThorServerHandler";

    ExecutorService               executor;

    public AbstractServer(URL url, ThorChannelHandler handler) throws RemotingException {
        super(url, handler);
        localAddress = getUrl().toInetSocketAddress();
        String host = url.getParameter(Constants.ANYHOST_KEY, false) || NetUtils.isInvalidLocalHost(getUrl().getHost()) ? NetUtils.ANYHOST : getUrl().getHost();
        bindAddress = new InetSocketAddress(host, getUrl().getPort());
        this.accepts = url.getParameter(Constants.ACCEPTS_KEY, Constants.DEFAULT_ACCEPTS);
        this.idleTimeout = url.getParameter(Constants.IDLE_TIMEOUT_KEY, Constants.DEFAULT_IDLE_TIMEOUT);
        try {
            doOpen();
            if (logger.isInfoEnabled()) {
                logger.info("Start " + getClass().getSimpleName() + " bind " + getBindAddress() + ", export "
                            + getLocalAddress());
            }
        } catch (Throwable t) {
            throw new RemotingException(url.toInetSocketAddress(), null, "Failed to bind " + getClass().getSimpleName()
                                                                         + " on " + getLocalAddress() + ", cause: "
                                                                         + t.getMessage(), t);
        }
        if (handler instanceof WrappedChannelHandler) {
            executor = ((WrappedChannelHandler) handler).getExecutor();
        }
    }

    protected abstract void doOpen() throws Throwable;

    protected abstract void doClose() throws Throwable;

    public void reset(URL url) {
        if (url == null) {
            return;
        }
        try {
            if (url.hasParameter(Constants.ACCEPTS_KEY)) {
                int a = url.getParameter(Constants.ACCEPTS_KEY, 0);
                if (a > 0) {
                    this.accepts = a;
                }
            }
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
        try {
            if (url.hasParameter(Constants.IDLE_TIMEOUT_KEY)) {
                int t = url.getParameter(Constants.IDLE_TIMEOUT_KEY, 0);
                if (t > 0) {
                    this.idleTimeout = t;
                }
            }
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
        try {
            if (url.hasParameter(Constants.THREADS_KEY) && executor instanceof ThreadPoolExecutor
                && !executor.isShutdown()) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                int threads = url.getParameter(Constants.THREADS_KEY, 0);
                int max = threadPoolExecutor.getMaximumPoolSize();
                int core = threadPoolExecutor.getCorePoolSize();
                if (threads > 0 && (threads != max || threads != core)) {
                    if (threads < core) {
                        threadPoolExecutor.setCorePoolSize(threads);
                        if (core == max) {
                            threadPoolExecutor.setMaximumPoolSize(threads);
                        }
                    } else {
                        threadPoolExecutor.setMaximumPoolSize(threads);
                        if (core == max) {
                            threadPoolExecutor.setCorePoolSize(threads);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
        super.setUrl(getUrl().addParameters(url.getParameters()));
    }

    public void send(Object message, boolean sent) throws RemotingException {
        Collection<ThorChannel> channels = getChannels();
        for (ThorChannel channel : channels) {
            if (channel.isConnected()) {
                channel.send(message, sent);
            }
        }
    }

    public void close() {
        if (logger.isInfoEnabled()) {
            logger.info("Close " + getClass().getSimpleName() + " bind " + getBindAddress() + ", export "
                        + getLocalAddress());
        }
        ExecutorUtil.shutdownNow(executor, 100);
        try {
            super.close();
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            doClose();
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public void close(int timeout) {
        ExecutorUtil.gracefulShutdown(executor, timeout);
        close();
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }

    public int getAccepts() {
        return accepts;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    @Override
    public void connected(ThorChannel ch) throws RemotingException {
        Collection<ThorChannel> channels = getChannels();
        if (accepts > 0 && channels.size() > accepts) {
            logger.error("Close channel " + ch + ", cause: The server " + ch.getLocalAddress()
                         + " connections greater than max config " + accepts);
            ch.close();
            return;
        }
        super.connected(ch);
    }

    @Override
    public void disconnected(ThorChannel ch) throws RemotingException {
        Collection<ThorChannel> channels = getChannels();
        if (channels.size() == 0) {
            logger.warn("All clients has discontected from " + ch.getLocalAddress()
                        + ". You can graceful shutdown now.");
        }
        super.disconnected(ch);
    }

}
