package com.mob.thor.rpc.remoting.api.exchange;

import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;

public interface ExchangeChannel extends ThorChannel {

    /**
     * send request.
     * 
     * @param request
     * @return response future
     * @throws RemotingException
     */
    ResponseFuture request(Object request) throws RemotingException;

    /**
     * send request.
     * 
     * @param request
     * @param timeout
     * @return response future
     * @throws RemotingException
     */
    ResponseFuture request(Object request, int timeout) throws RemotingException;

    /**
     * get message handler.
     * 
     * @return message handler
     */
    ExchangeHandler getExchangeHandler();

    /**
     * graceful close.
     * 
     * @param timeout
     */
    void close(int timeout);

}
