package com.mob.thor.rpc.common.threadpool.support.cached;

import java.util.concurrent.*;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.threadpool.ThreadPool;
import com.mob.thor.rpc.common.threadpool.support.AbortPolicyWithReport;
import com.mob.thor.rpc.common.utils.NamedThreadFactory;

/**
 * 此线程池可伸缩，线程空闲一分钟后回收，新请求重新创建线程，来源于：<code>Executors.newCachedThreadPool()</code>
 * 
 * @see java.util.concurrent.Executors#newCachedThreadPool()
 * @author william.liangf
 */
public class CachedThreadPool implements ThreadPool {

    public Executor getExecutor(URL url) {
        String name = url.getParameter(Constants.THREAD_NAME_KEY, Constants.DEFAULT_THREAD_NAME);
        int cores = url.getParameter(Constants.CORE_THREADS_KEY, Constants.DEFAULT_CORE_THREADS);
        int threads = url.getParameter(Constants.THREADS_KEY, Integer.MAX_VALUE);
        int queues = url.getParameter(Constants.QUEUES_KEY, Constants.DEFAULT_QUEUES);
        int alive = url.getParameter(Constants.ALIVE_KEY, Constants.DEFAULT_ALIVE);
        return new ThreadPoolExecutor(
                                      cores,
                                      threads,
                                      alive,
                                      TimeUnit.MILLISECONDS,
                                      queues == 0 ? new SynchronousQueue<Runnable>() : (queues < 0 ? new LinkedBlockingQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(
                                                                                                                                                                             queues)),
                                      new NamedThreadFactory(name, true), new AbortPolicyWithReport(name, url));
    }

}
