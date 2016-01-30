package com.mob.thor.rpc.api;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.SPI;

/**
 * InvokerListener. (SPI, Singleton, ThreadSafe)
 * 
 * @author zxc
 */
@SPI
public interface InvokerListener {

    /**
     * The invoker referred
     * 
     * @see com.mob.thor.rpc.Protocol#refer(Class, URL)
     * @param invoker
     * @throws RpcException
     */
    void referred(Invoker<?> invoker) throws RpcException;

    /**
     * The invoker destroyed.
     * 
     * @see com.mob.thor.rpc.Invoker#destroy()
     * @param invoker
     */
    void destroyed(Invoker<?> invoker);
}
