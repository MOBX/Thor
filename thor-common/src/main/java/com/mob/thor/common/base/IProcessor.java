/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.base;

import java.io.Serializable;

/**
 * @author zxc Dec 9, 2015 10:04:23 AM
 */
public interface IProcessor<T extends Serializable> extends Runnable {

    void run();
}
