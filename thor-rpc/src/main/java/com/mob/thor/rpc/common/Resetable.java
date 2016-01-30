/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.rpc.common;

/**
 * Resetable.
 * 
 * @author zxc
 */
public interface Resetable {

    /**
     * reset.
     * 
     * @param url
     */
    void reset(URL url);
}