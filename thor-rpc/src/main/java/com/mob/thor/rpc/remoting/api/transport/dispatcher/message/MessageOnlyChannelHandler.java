package com.mob.thor.rpc.remoting.api.transport.dispatcher.message;

import java.util.concurrent.ExecutorService;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.*;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.ChannelEventRunnable;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.ChannelEventRunnable.ChannelState;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.WrappedChannelHandler;

public class MessageOnlyChannelHandler extends WrappedChannelHandler {

    public MessageOnlyChannelHandler(ThorChannelHandler handler, URL url) {
        super(handler, url);
    }

    public void received(ThorChannel channel, Object message) throws RemotingException {
        ExecutorService cexecutor = executor;
        if (cexecutor == null || cexecutor.isShutdown()) {
            cexecutor = SHARED_EXECUTOR;
        }
        try {
            cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.RECEIVED, message));
        } catch (Throwable t) {
            throw new ExecutionException(message, channel, getClass() + " error when process received event .", t);
        }
    }

}
