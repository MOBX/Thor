/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.base;

import java.nio.channels.Channel;

import com.mob.thor.common.exception.RemotingException;

/**
 * @author zxc Dec 8, 2015 5:01:50 PM
 */
public interface IChannelHandler {

    /**
     * on channel connected.
     * 
     * @param channel channel.
     */
    void connected(Channel channel) throws RemotingException;

    /**
     * on channel disconnected.
     * 
     * @param channel channel.
     */
    void disconnected(Channel channel) throws RemotingException;

    /**
     * on message sent.
     * 
     * @param channel channel.
     * @param message message.
     */
    void sent(Channel channel, Object message) throws RemotingException;

    /**
     * on message received.
     * 
     * @param channel channel.
     * @param message message.
     */
    void received(Channel channel, Object message) throws RemotingException;

    /**
     * on exception caught.
     * 
     * @param channel channel.
     * @param exception exception.
     */
    void caught(Channel channel, Throwable exception) throws RemotingException;
}
