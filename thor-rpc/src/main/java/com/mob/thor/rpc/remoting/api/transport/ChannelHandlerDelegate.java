package com.mob.thor.rpc.remoting.api.transport;

import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

/**
 * @author zxc Jan 4, 2016 11:28:41 AM
 */
public interface ChannelHandlerDelegate extends ThorChannelHandler {

    public ThorChannelHandler getHandler();
}
