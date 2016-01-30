package com.mob.thor.rpc.remoting.api.transport;

import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.exchange.support.MultiMessage;

public class MultiMessageHandler extends AbstractChannelHandlerDelegate {

    public MultiMessageHandler(ThorChannelHandler handler) {
        super(handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void received(ThorChannel channel, Object message) throws RemotingException {
        if (message instanceof MultiMessage) {
            MultiMessage list = (MultiMessage) message;
            for (Object obj : list) {
                handler.received(channel, obj);
            }
        } else {
            handler.received(channel, message);
        }
    }
}
