package com.mob.thor.rpc.common.threadpool.support.limited;

import java.util.concurrent.*;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.threadpool.ThreadPool;
import com.mob.thor.rpc.common.threadpool.support.AbortPolicyWithReport;
import com.mob.thor.rpc.common.utils.NamedThreadFactory;

/**
 * 此线程池一直增长，直到上限，增长后不收缩。
 * 
 * @author zxc Jan 6, 2016 5:10:30 PM
 */
public class LimitedThreadPool implements ThreadPool {

    public Executor getExecutor(URL url) {
        String name = url.getParameter(Constants.THREAD_NAME_KEY, Constants.DEFAULT_THREAD_NAME);
        int cores = url.getParameter(Constants.CORE_THREADS_KEY, Constants.DEFAULT_CORE_THREADS);
        int threads = url.getParameter(Constants.THREADS_KEY, Constants.DEFAULT_THREADS);
        int queues = url.getParameter(Constants.QUEUES_KEY, Constants.DEFAULT_QUEUES);
        return new ThreadPoolExecutor(
                                      cores,
                                      threads,
                                      Long.MAX_VALUE,
                                      TimeUnit.MILLISECONDS,
                                      queues == 0 ? new SynchronousQueue<Runnable>() : (queues < 0 ? new LinkedBlockingQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(
                                                                                                                                                                             queues)),
                                      new NamedThreadFactory(name, true), new AbortPolicyWithReport(name, url));
    }

}
