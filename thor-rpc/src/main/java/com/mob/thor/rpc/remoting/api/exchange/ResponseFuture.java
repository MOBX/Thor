package com.mob.thor.rpc.remoting.api.exchange;

import com.mob.thor.rpc.remoting.api.RemotingException;

/**
 * Future. (API/SPI, Prototype, ThreadSafe)
 * 
 * @see com.mob.thor.remoting.exchange.ExchangeChannel#request(Object)
 * @see com.mob.thor.remoting.exchange.ExchangeChannel#request(Object, int)
 */
public interface ResponseFuture {

    /**
     * get result.
     * 
     * @return result.
     */
    Object get() throws RemotingException;

    /**
     * get result with the specified timeout.
     * 
     * @param timeoutInMillis timeout.
     * @return result.
     */
    Object get(int timeoutInMillis) throws RemotingException;

    /**
     * set callback.
     * 
     * @param callback
     */
    void setCallback(ResponseCallback callback);

    /**
     * check is done.
     * 
     * @return done or not.
     */
    boolean isDone();
}
