package com.mob.thor.rpc.remoting.api.exchange.support;

import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeChannel;

/**
 * Replier. (API, Prototype, ThreadSafe)
 * 
 * @author zxc
 */
public interface Replier<T> {

    /**
     * reply.
     * 
     * @param channel
     * @param request
     * @return response
     * @throws RemotingException
     */
    Object reply(ExchangeChannel channel, T request) throws RemotingException;
}
