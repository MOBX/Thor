package com.mob.thor.rpc.remoting.api.transport;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

public abstract class AbstractChannel extends AbstractPeer implements ThorChannel {

    public AbstractChannel(URL url, ThorChannelHandler handler) {
        super(url, handler);
    }

    public void send(Object message, boolean sent) throws RemotingException {
        if (isClosed()) {
            throw new RemotingException(this, "Failed to send message "
                                              + (message == null ? "" : message.getClass().getName()) + ":" + message
                                              + ", cause: Channel closed. channel: " + getLocalAddress() + " -> "
                                              + getRemoteAddress());
        }
    }

    @Override
    public String toString() {
        return getLocalAddress() + " -> " + getRemoteAddress();
    }
}
