/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.cons;

/**
 * @author zxc Dec 8, 2015 1:37:30 PM
 */
public interface JSyncConstant {

    public static final int DEFAULT_IO_THREADS              = Runtime.getRuntime().availableProcessors() + 1;

    public static final int DEFAULT_RECONNECT_PERIOD        = 2000;

    public static final int clientChannelMaxIdleTimeSeconds = 10;

    public static final int serverChannelMaxIdleTimeSeconds = 10;
}
