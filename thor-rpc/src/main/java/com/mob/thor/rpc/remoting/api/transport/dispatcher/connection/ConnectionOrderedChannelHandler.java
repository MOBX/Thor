package com.mob.thor.rpc.remoting.api.transport.dispatcher.connection;

import java.util.concurrent.*;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.threadpool.support.AbortPolicyWithReport;
import com.mob.thor.rpc.common.utils.NamedThreadFactory;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.ChannelEventRunnable;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.ChannelEventRunnable.ChannelState;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.WrappedChannelHandler;

public class ConnectionOrderedChannelHandler extends WrappedChannelHandler {

    protected final ThreadPoolExecutor connectionExecutor;
    private final int                  queuewarninglimit;

    public ConnectionOrderedChannelHandler(ThorChannelHandler handler, URL url) {
        super(handler, url);
        String threadName = url.getParameter(Constants.THREAD_NAME_KEY, Constants.DEFAULT_THREAD_NAME);
        connectionExecutor = new ThreadPoolExecutor(
                                                    1,
                                                    1,
                                                    0L,
                                                    TimeUnit.MILLISECONDS,
                                                    new LinkedBlockingQueue<Runnable>(
                                                                                      url.getPositiveParameter(Constants.CONNECT_QUEUE_CAPACITY,
                                                                                                               Integer.MAX_VALUE)),
                                                    new NamedThreadFactory(threadName, true),
                                                    new AbortPolicyWithReport(threadName, url)); // FIXME
                                                                                                 // 没有地方释放connectionExecutor！
        queuewarninglimit = url.getParameter(Constants.CONNECT_QUEUE_WARNING_SIZE,
                                             Constants.DEFAULT_CONNECT_QUEUE_WARNING_SIZE);
    }

    public void connected(ThorChannel channel) throws RemotingException {
        try {
            checkQueueLength();
            connectionExecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.CONNECTED));
        } catch (Throwable t) {
            throw new com.mob.thor.rpc.remoting.api.ExecutionException(
                                                                       "connect event",
                                                                       channel,
                                                                       getClass()
                                                                               + " error when process connected event .",
                                                                       t);
        }
    }

    public void disconnected(ThorChannel channel) throws RemotingException {
        try {
            checkQueueLength();
            connectionExecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.DISCONNECTED));
        } catch (Throwable t) {
            throw new com.mob.thor.rpc.remoting.api.ExecutionException(
                                                                       "disconnected event",
                                                                       channel,
                                                                       getClass()
                                                                               + " error when process disconnected event .",
                                                                       t);
        }
    }

    public void received(ThorChannel channel, Object message) throws RemotingException {
        ExecutorService cexecutor = executor;
        if (cexecutor == null || cexecutor.isShutdown()) {
            cexecutor = SHARED_EXECUTOR;
        }
        try {
            cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.RECEIVED, message));
        } catch (Throwable t) {
            throw new com.mob.thor.rpc.remoting.api.ExecutionException(
                                                                       message,
                                                                       channel,
                                                                       getClass()
                                                                               + " error when process received event .",
                                                                       t);
        }
    }

    public void caught(ThorChannel channel, Throwable exception) throws RemotingException {
        ExecutorService cexecutor = executor;
        if (cexecutor == null || cexecutor.isShutdown()) {
            cexecutor = SHARED_EXECUTOR;
        }
        try {
            cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.CAUGHT, exception));
        } catch (Throwable t) {
            throw new com.mob.thor.rpc.remoting.api.ExecutionException("caught event", channel,
                                                                       getClass()
                                                                               + " error when process caught event .",
                                                                       t);
        }
    }

    private void checkQueueLength() {
        if (connectionExecutor.getQueue().size() > queuewarninglimit) {
            logger.warn(new IllegalThreadStateException("connectionordered channel handler `queue size: "
                                                        + connectionExecutor.getQueue().size()
                                                        + " exceed the warning limit number :" + queuewarninglimit));
        }
    }
}
