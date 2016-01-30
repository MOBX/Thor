package com.mob.thor.rpc.remoting.api.transport;

import com.mob.thor.common.utils.Assert;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

public abstract class AbstractChannelHandlerDelegate implements ChannelHandlerDelegate {

    protected ThorChannelHandler handler;

    protected AbstractChannelHandlerDelegate(ThorChannelHandler handler) {
        Assert.notNull(handler, "handler == null");
        this.handler = handler;
    }

    public ThorChannelHandler getHandler() {
        if (handler instanceof ChannelHandlerDelegate) {
            return ((ChannelHandlerDelegate) handler).getHandler();
        }
        return handler;
    }

    public void connected(ThorChannel channel) throws RemotingException {
        handler.connected(channel);
    }

    public void disconnected(ThorChannel channel) throws RemotingException {
        handler.disconnected(channel);
    }

    public void sent(ThorChannel channel, Object message) throws RemotingException {
        handler.sent(channel, message);
    }

    public void received(ThorChannel channel, Object message) throws RemotingException {
        handler.received(channel, message);
    }

    public void caught(ThorChannel channel, Throwable exception) throws RemotingException {
        handler.caught(channel, exception);
    }
}
