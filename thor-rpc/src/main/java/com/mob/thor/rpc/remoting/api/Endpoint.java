package com.mob.thor.rpc.remoting.api;

import java.net.InetSocketAddress;

import com.mob.thor.rpc.common.URL;

/**
 * Endpoint. (API/SPI, Prototype, ThreadSafe)
 * 
 * @see com.mob.thor.ThorChannel.remoting.Channel
 * @see com.mob.thor.remoting.Client
 * @see com.mob.thor.remoting.Server
 * @author zxc
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
    ThorChannelHandler getChannelHandler();

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
