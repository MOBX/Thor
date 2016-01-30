package com.mob.thor.rpc.common.threadpool.support.fixed;

import java.util.concurrent.*;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.threadpool.ThreadPool;
import com.mob.thor.rpc.common.threadpool.support.AbortPolicyWithReport;
import com.mob.thor.rpc.common.utils.NamedThreadFactory;

/**
 * 此线程池启动时即创建固定大小的线程数，不做任何伸缩，来源于：<code>Executors.newFixedThreadPool()</code>
 * 
 * @see java.util.concurrent.Executors#newFixedThreadPool(int)
 * @author zxc
 */
public class FixedThreadPool implements ThreadPool {

    public Executor getExecutor(URL url) {
        String name = url.getParameter(Constants.THREAD_NAME_KEY, Constants.DEFAULT_THREAD_NAME);
        int threads = url.getParameter(Constants.THREADS_KEY, Constants.DEFAULT_THREADS);
        int queues = url.getParameter(Constants.QUEUES_KEY, Constants.DEFAULT_QUEUES);
        return new ThreadPoolExecutor(
                                      threads,
                                      threads,
                                      0,
                                      TimeUnit.MILLISECONDS,
                                      queues == 0 ? new SynchronousQueue<Runnable>() : (queues < 0 ? new LinkedBlockingQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(
                                                                                                                                                                             queues)),
                                      new NamedThreadFactory(name, true), new AbortPolicyWithReport(name, url));
    }

}
