/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.protocol;

import java.net.InetSocketAddress;
import java.net.URL;

import com.mob.thor.common.exception.RemotingException;

/**
 * @author zxc Dec 8, 2015 1:51:42 PM
 */
public interface Endpoint {

    /**
     * get url.
     * 
     * @return url
     */
    URL getUrl();

    /**
     * get channel handler.
     * 
     * @return channel handler
     */
    // ChannelHandler getChannelHandler();

    /**
     * get local address.
     * 
     * @return local address.
     */
    InetSocketAddress getLocalAddress();

    /**
     * send message.
     * 
     * @param message
     * @throws RemotingException
     */
    void send(Object message) throws RemotingException;

    /**
     * send message.
     * 
     * @param message
     * @param sent 是否已发送完成
     */
    void send(Object message, boolean sent) throws RemotingException;

    /**
     * close the channel.
     */
    void close();

    /**
     * Graceful close the channel.
     */
    void close(int timeout);

    /**
     * is closed.
     * 
     * @return closed
     */
    boolean isClosed();
}
