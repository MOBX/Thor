package com.mob.thor.rpc.remoting.api.transport.dispatcher.all;

import java.util.concurrent.ExecutorService;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.*;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.ChannelEventRunnable;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.ChannelEventRunnable.ChannelState;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.WrappedChannelHandler;

public class AllChannelHandler extends WrappedChannelHandler {

    public AllChannelHandler(ThorChannelHandler handler, URL url) {
        super(handler, url);
    }

    public void connected(ThorChannel channel) throws RemotingException {
        ExecutorService cexecutor = getExecutorService();
        try {
            cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.CONNECTED));
        } catch (Throwable t) {
            throw new ExecutionException("connect event", channel,
                                         getClass() + " error when process connected event .", t);
        }
    }

    public void disconnected(ThorChannel channel) throws RemotingException {
        ExecutorService cexecutor = getExecutorService();
        try {
            cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.DISCONNECTED));
        } catch (Throwable t) {
            throw new ExecutionException("disconnect event", channel, getClass()
                                                                      + " error when process disconnected event .", t);
        }
    }

    public void received(ThorChannel channel, Object message) throws RemotingException {
        ExecutorService cexecutor = getExecutorService();
        try {
            cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.RECEIVED, message));
        } catch (Throwable t) {
            throw new ExecutionException(message, channel, getClass() + " error when process received event .", t);
        }
    }

    public void caught(ThorChannel channel, Throwable exception) throws RemotingException {
        ExecutorService cexecutor = getExecutorService();
        try {
            cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.CAUGHT, exception));
        } catch (Throwable t) {
            throw new ExecutionException("caught event", channel, getClass() + " error when process caught event .", t);
        }
    }

    private ExecutorService getExecutorService() {
        ExecutorService cexecutor = executor;
        if (cexecutor == null || cexecutor.isShutdown()) {
            cexecutor = SHARED_EXECUTOR;
        }
        return cexecutor;
    }
}
