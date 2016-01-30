package com.mob.thor.rpc.common.threadpool;

import java.util.concurrent.Executor;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.Adaptive;
import com.mob.thor.rpc.common.extension.SPI;

@SPI("fixed")
public interface ThreadPool {

    /**
     * 线程池
     * 
     * @param url 线程参数
     * @return 线程池
     */
    @Adaptive({ Constants.THREADPOOL_KEY })
    Executor getExecutor(URL url);
}
