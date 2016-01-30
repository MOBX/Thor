package com.mob.thor.rpc.api.filter;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Activate;

@Activate(group = Constants.PROVIDER, order = -30000)
public class ClassLoaderFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        ClassLoader ocl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(invoker.getInterface().getClassLoader());
        try {
            return invoker.invoke(invocation);
        } finally {
            Thread.currentThread().setContextClassLoader(ocl);
        }
    }
}
