package com.mob.thor.rpc.remoting.api.transport.dispatcher.execution;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.ChannelEventRunnable;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.ChannelEventRunnable.ChannelState;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.WrappedChannelHandler;

public class ExecutionChannelHandler extends WrappedChannelHandler {

    public ExecutionChannelHandler(ThorChannelHandler handler, URL url) {
        super(handler, url);
    }

    public void connected(ThorChannel channel) throws RemotingException {
        executor.execute(new ChannelEventRunnable(channel, handler, ChannelState.CONNECTED));
    }

    public void disconnected(ThorChannel channel) throws RemotingException {
        executor.execute(new ChannelEventRunnable(channel, handler, ChannelState.DISCONNECTED));
    }

    public void received(ThorChannel channel, Object message) throws RemotingException {
        executor.execute(new ChannelEventRunnable(channel, handler, ChannelState.RECEIVED, message));
    }

    public void caught(ThorChannel channel, Throwable exception) throws RemotingException {
        executor.execute(new ChannelEventRunnable(channel, handler, ChannelState.CAUGHT, exception));
    }

}
