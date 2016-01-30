package com.mob.thor.rpc.remoting.api.exchange;

import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.telnet.TelnetHandler;

public interface ExchangeHandler extends ThorChannelHandler, TelnetHandler {

    /**
     * reply.
     * 
     * @param channel
     * @param request
     * @return response
     * @throws RemotingException
     */
    Object reply(ExchangeChannel channel, Object request) throws RemotingException;

}
