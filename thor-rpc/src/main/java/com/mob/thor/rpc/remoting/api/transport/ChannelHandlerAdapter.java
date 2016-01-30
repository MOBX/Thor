package com.mob.thor.rpc.remoting.api.transport;

import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

public class ChannelHandlerAdapter implements ThorChannelHandler {

    public void connected(ThorChannel channel) throws RemotingException {
    }

    public void disconnected(ThorChannel channel) throws RemotingException {
    }

    public void sent(ThorChannel channel, Object message) throws RemotingException {
    }

    public void received(ThorChannel channel, Object message) throws RemotingException {
    }

    public void caught(ThorChannel channel, Throwable exception) throws RemotingException {
    }
}
