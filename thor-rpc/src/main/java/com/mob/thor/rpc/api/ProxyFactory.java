package com.mob.thor.rpc.api;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.Adaptive;
import com.mob.thor.rpc.common.extension.SPI;

/**
 * ProxyFactory. (API/SPI, Singleton, ThreadSafe)
 * 
 * @author zxc
 */
@SPI("javassist")
public interface ProxyFactory {

    /**
     * create proxy.
     * 
     * @param invoker
     * @return proxy
     */
    @Adaptive({ Constants.PROXY_KEY })
    <T> T getProxy(Invoker<T> invoker) throws RpcException;

    /**
     * create invoker.
     * 
     * @param <T>
     * @param proxy
     * @param type
     * @param url
     * @return invoker
     */
    @Adaptive({ Constants.PROXY_KEY })
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException;
}
