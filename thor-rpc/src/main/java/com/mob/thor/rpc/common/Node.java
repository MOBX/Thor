/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.rpc.common;

/**
 * Node. (API/SPI, Prototype, ThreadSafe)
 * 
 * @author william.liangf
 */
public interface Node {

    /**
     * get url.
     * 
     * @return url.
     */
    URL getUrl();

    /**
     * is available.
     * 
     * @return available.
     */
    boolean isAvailable();

    /**
     * destroy.
     */
    void destroy();
}
