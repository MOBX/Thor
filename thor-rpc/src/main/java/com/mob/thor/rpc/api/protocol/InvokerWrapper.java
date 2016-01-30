package com.mob.thor.rpc.api.protocol;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.common.URL;

public class InvokerWrapper<T> implements Invoker<T> {

    private final Invoker<T> invoker;

    private final URL        url;

    public InvokerWrapper(Invoker<T> invoker, URL url) {
        this.invoker = invoker;
        this.url = url;
    }

    public Class<T> getInterface() {
        return invoker.getInterface();
    }

    public URL getUrl() {
        return url;
    }

    public boolean isAvailable() {
        return invoker.isAvailable();
    }

    public Result invoke(Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    public void destroy() {
        invoker.destroy();
    }
}
