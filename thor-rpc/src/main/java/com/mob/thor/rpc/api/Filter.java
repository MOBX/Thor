package com.mob.thor.rpc.api;

import com.mob.thor.rpc.common.extension.SPI;

/**
 * Filter. (SPI, Singleton, ThreadSafe)
 * 
 * @author zxc
 */
@SPI
public interface Filter {

    /**
     * do invoke filter. <code>
     * // before filter
     * Result result = invoker.invoke(invocation);
     * // after filter
     * return result;
     * </code>
     * 
     * @see com.mob.thor.rpc.Invoker#invoke(Invocation)
     * @param invoker service
     * @param invocation invocation.
     * @return invoke result.
     * @throws RpcException
     */
    Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException;
}
