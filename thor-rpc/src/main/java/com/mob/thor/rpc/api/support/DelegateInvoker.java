package com.mob.thor.rpc.api.support;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.common.URL;

public abstract class DelegateInvoker<T> implements Invoker<T> {

    protected final Invoker<T> invoker;

    public DelegateInvoker(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    public Class<T> getInterface() {
        return invoker.getInterface();
    }

    public URL getUrl() {
        return invoker.getUrl();
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
