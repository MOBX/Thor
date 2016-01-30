package com.mob.thor.rpc.remoting.api;

import java.net.InetSocketAddress;

/**
 * Channel. (API/SPI, Prototype, ThreadSafe)
 * 
 * @see com.mob.thor.remoting.Client
 * @see com.mob.thor.remoting.Server#getChannels()
 * @see com.mob.thor.remoting.Server#getChannel(InetSocketAddress)
 * @author zxc
 */
public interface ThorChannel extends Endpoint {

    /**
     * get remote address.
     * 
     * @return remote address.
     */
    InetSocketAddress getRemoteAddress();

    /**
     * is connected.
     * 
     * @return connected
     */
    boolean isConnected();

    /**
     * has attribute.
     * 
     * @param key key.
     * @return has or has not.
     */
    boolean hasAttribute(String key);

    /**
     * get attribute.
     * 
     * @param key key.
     * @return value.
     */
    Object getAttribute(String key);

    /**
     * set attribute.
     * 
     * @param key key.
     * @param value value.
     */
    void setAttribute(String key, Object value);

    /**
     * remove attribute.
     * 
     * @param key key.
     */
    void removeAttribute(String key);

}
