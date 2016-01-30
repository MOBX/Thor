package com.mob.thor.rpc.remoting.api;

import com.mob.thor.rpc.common.extension.SPI;

/**
 * ChannelHandler. (API, Prototype, ThreadSafe)
 * 
 * @see com.mob.thor.remoting.Transporter#bind(com.mob.thor.common.URL, ChannelHandler)
 * @see com.mob.thor.remoting.Transporter#connect(com.mob.thor.common.URL, ChannelHandler)
 * @author zxc
 */
@SPI
public interface ThorChannelHandler {

    /**
     * on channel connected.
     * 
     * @param channel channel.
     */
    void connected(ThorChannel channel) throws RemotingException;

    /**
     * on channel disconnected.
     * 
     * @param channel channel.
     */
    void disconnected(ThorChannel channel) throws RemotingException;

    /**
     * on message sent.
     * 
     * @param channel channel.
     * @param message message.
     */
    void sent(ThorChannel channel, Object message) throws RemotingException;

    /**
     * on message received.
     * 
     * @param channel channel.
     * @param message message.
     */
    void received(ThorChannel channel, Object message) throws RemotingException;

    /**
     * on exception caught.
     * 
     * @param channel channel.
     * @param exception exception.
     */
    void caught(ThorChannel channel, Throwable exception) throws RemotingException;
}
