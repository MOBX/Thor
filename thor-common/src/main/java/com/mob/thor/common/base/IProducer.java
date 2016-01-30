/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.base;

/**
 * @author zxc Dec 9, 2015 4:36:00 PM
 */
public interface IProducer {

    void send(String topic, Object message);
    
    void close();
}
