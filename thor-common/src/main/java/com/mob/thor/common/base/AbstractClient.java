/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.base;

import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mob.thor.common.cons.JSyncConstant;
import com.mob.thor.common.exception.RemotingException;
import com.mob.thor.common.protocol.JSyncChannel;
import com.mob.thor.common.utils.NamedThreadFactory;
import com.mob.thor.common.utils.NetUtils;
import com.mob.thor.common.utils.URL;

/**
 * @author zxc Dec 8, 2015 1:42:15 PM
 */
public abstract class AbstractClient implements JSyncConstant, JSyncChannel {

    private static final Logger                      logger                   = LoggerFactory.getLogger(AbstractClient.class);

    private final Lock                               connectLock              = new ReentrantLock();

    private static final ScheduledThreadPoolExecutor reconnectExecutorService = new ScheduledThreadPoolExecutor(
                                                                                                                2,
                                                                                                                new NamedThreadFactory(
                                                                                                                                       "JSyncClientReconnectTimer",
                                                                                                                                       true));

    private volatile ScheduledFuture<?>              reconnectExecutorFuture  = null;
    // 重连的次数
    private final AtomicInteger                      reconnect_count          = new AtomicInteger(0);
    // 重连的error日志是否已经被调用过.
    private final AtomicBoolean                      reconnect_error_log_flag = new AtomicBoolean(false);
    // 重连warning的间隔.(waring多少次之后，warning一次) //for test
    private final int                                reconnect_warning_period;
    // the last successed connected time
    private long                                     lastConnectedTime        = System.currentTimeMillis();

    private volatile URL                             url;
    private volatile boolean                         closed;

    public AbstractClient(URL url) throws RemotingException {
        // 默认重连间隔2s，1800表示1小时warning一次.
        reconnect_warning_period = 1800;// url.getParameter("reconnect.waring.period", 1800);

        try {
            doOpen();
        } catch (Throwable t) {
            close();
            throw new RemotingException(url.toInetSocketAddress(), null, "Failed to start "
                                                                         + getClass().getSimpleName() + " "
                                                                         + NetUtils.getLocalAddress()
                                                                         + " connect to the server "
                                                                         + getRemoteAddress() + ", cause: "
                                                                         + t.getMessage(), t);
        }
        try {
            connect();
            logger.info("Start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
                        + " connect to the server " + getRemoteAddress());
        } catch (RemotingException t) {
            close();
            logger.warn("Failed to start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
                        + " connect to the server " + getRemoteAddress()
                        + " (check == false, ignore and retry later!), cause: " + t.getMessage(), t);
        } catch (Throwable t) {
            close();
            throw new RemotingException(url.toInetSocketAddress(), null, "Failed to start "
                                                                         + getClass().getSimpleName() + " "
                                                                         + NetUtils.getLocalAddress()
                                                                         + " connect to the server "
                                                                         + getRemoteAddress() + ", cause: "
                                                                         + t.getMessage(), t);
        }
    }

    /**
     * init reconnect thread
     */
    private synchronized void initConnectStatusCheckCommand() {
        // reconnect=false to close reconnect
        int reconnect = 2000;
        if (reconnect > 0 && (reconnectExecutorFuture == null || reconnectExecutorFuture.isCancelled())) {
            Runnable connectStatusCheckCommand = new Runnable() {

                public void run() {
                    try {
                        if (!isConnected()) {
                            connect();
                        } else {
                            lastConnectedTime = System.currentTimeMillis();
                        }
                    } catch (Throwable t) {
                        String errorMsg = "client reconnect to " + url.getAddress() + " find error . url: " + getUrl();
                        // wait registry sync provider list
                        // if (System.currentTimeMillis() - lastConnectedTime > shutdown_timeout) {
                        // if (!reconnect_error_log_flag.get()) {
                        // reconnect_error_log_flag.set(true);
                        // logger.error(errorMsg, t);
                        // return;
                        // }
                        // }
                        if (reconnect_count.getAndIncrement() % reconnect_warning_period == 0) {
                            logger.warn(errorMsg, t);
                        }
                    }
                }
            };
            reconnectExecutorFuture = reconnectExecutorService.scheduleWithFixedDelay(connectStatusCheckCommand,
                                                                                      reconnect, reconnect,
                                                                                      TimeUnit.MILLISECONDS);
        }
    }

    private synchronized void destroyConnectStatusCheckCommand() {
        try {
            if (reconnectExecutorFuture != null && !reconnectExecutorFuture.isDone()) {
                reconnectExecutorFuture.cancel(true);
                reconnectExecutorService.purge();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public InetSocketAddress getConnectAddress() {
        return new InetSocketAddress(NetUtils.filterLocalHost(getUrl().getHost()), getUrl().getPort());
    }

    public InetSocketAddress getRemoteAddress() {
        JSyncChannel channel = getChannel();
        if (channel == null) {
            // return getUrl().toInetSocketAddress();
        }
        return channel.getRemoteAddress();
    }

    public InetSocketAddress getLocalAddress() {
        JSyncChannel channel = getChannel();
        if (channel == null) return InetSocketAddress.createUnresolved(NetUtils.getLocalHost(), 0);
        return channel.getLocalAddress();
    }

    public boolean isConnected() {
        JSyncChannel channel = getChannel();
        if (channel == null) return false;
        return channel.isConnected();
    }

    public Object getAttribute(String key) {
        JSyncChannel channel = getChannel();
        if (channel == null) return null;
        return channel.getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        JSyncChannel channel = getChannel();
        if (channel == null) return;
        channel.setAttribute(key, value);
    }

    public void removeAttribute(String key) {
        JSyncChannel channel = getChannel();
        if (channel == null) return;
        channel.removeAttribute(key);
    }

    public boolean hasAttribute(String key) {
        JSyncChannel channel = getChannel();
        if (channel == null) return false;
        return channel.hasAttribute(key);
    }

    public void send(Object message, boolean sent) throws RemotingException {
        if (!isConnected()) {
            connect();
        }
        JSyncChannel channel = getChannel();
        // TODO getChannel返回的状态是否包含null需要改进
        if (channel == null || !channel.isConnected()) {
            throw new RemotingException(this, "message can not send, because channel is closed . url:" + url);
        }
        channel.send(message, sent);
    }

    protected void connect() throws RemotingException {
        connectLock.lock();
        try {
            if (isConnected()) {
                return;
            }
            initConnectStatusCheckCommand();
            doConnect();
            if (!isConnected()) {
                throw new RemotingException(this, "Failed connect to server " + getRemoteAddress() + " from "
                                                  + getClass().getSimpleName() + " " + NetUtils.getLocalHost()
                                                  + ", cause: Connect wait timeout: " + getTimeout() + "ms.");
            } else {
                logger.info("Successed connect to server " + getRemoteAddress() + " from " + getClass().getSimpleName()
                            + " " + NetUtils.getLocalHost() + ", channel is " + this.getChannel());
            }
            reconnect_count.set(0);
            reconnect_error_log_flag.set(false);
        } catch (RemotingException e) {
            throw e;
        } catch (Throwable e) {
            throw new RemotingException(this, "Failed connect to server " + getRemoteAddress() + " from "
                                              + getClass().getSimpleName() + " " + NetUtils.getLocalHost()
                                              + ", cause: " + e.getMessage(), e);
        } finally {
            connectLock.unlock();
        }
    }

    private String getTimeout() {
        return "200";
    }

    public void disconnect() {
        connectLock.lock();
        try {
            destroyConnectStatusCheckCommand();
            try {
                JSyncChannel channel = getChannel();
                if (channel != null) {
                    channel.close();
                }
            } catch (Throwable e) {
                logger.warn(e.getMessage(), e);
            }
            try {
                doDisConnect();
            } catch (Throwable e) {
                logger.warn(e.getMessage(), e);
            }
        } finally {
            connectLock.unlock();
        }
    }

    public void reconnect() throws RemotingException {
        disconnect();
        connect();
    }

    public void close() {
        try {
            disconnect();
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            doClose();
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + " [" + getLocalAddress() + " -> " + getRemoteAddress() + "]";
    }

    /**
     * Open client.
     * 
     * @throws Throwable
     */
    protected abstract void doOpen() throws Throwable;

    /**
     * Close client.
     * 
     * @throws Throwable
     */
    protected abstract void doClose() throws Throwable;

    /**
     * Connect to server.
     * 
     * @throws Throwable
     */
    protected abstract void doConnect() throws Throwable;

    /**
     * disConnect to server.
     * 
     * @throws Throwable
     */
    protected abstract void doDisConnect() throws Throwable;

    /**
     * Get the connected channel.
     * 
     * @return channel
     */
    protected abstract JSyncChannel getChannel();
}
