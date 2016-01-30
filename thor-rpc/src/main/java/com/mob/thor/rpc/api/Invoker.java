package com.mob.thor.rpc.api;

import com.mob.thor.rpc.common.Node;

/**
 * Invoker. (API/SPI, Prototype, ThreadSafe)
 * 
 * @see com.mob.thor.rpc.Protocol#refer(Class, com.mob.thor.common.URL)
 * @see com.mob.thor.rpc.InvokerListener
 * @see com.mob.thor.rpc.protocol.AbstractInvoker
 * @author zxc
 */
public interface Invoker<T> extends Node {

    /**
     * get service interface.
     * 
     * @return service interface.
     */
    Class<T> getInterface();

    /**
     * invoke.
     * 
     * @param invocation
     * @return result
     * @throws RpcException
     */
    Result invoke(Invocation invocation) throws RpcException;
}
