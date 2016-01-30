/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.protocol;

import java.net.InetSocketAddress;

/**
 * @author zxc Dec 8, 2015 1:50:15 PM
 */
public interface JSyncChannel extends Endpoint {

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
