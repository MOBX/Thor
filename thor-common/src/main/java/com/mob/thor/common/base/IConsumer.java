/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.base;

/**
 * @author zxc Dec 9, 2015 9:47:47 AM
 */
public interface IConsumer extends Runnable {

    IExecutor getExecutor();

    void setExecutor(IExecutor executor);

    void run();

    void close();
}
