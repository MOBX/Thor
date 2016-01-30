package com.mob.thor.rpc.api.filter;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Activate;

@Activate(group = Constants.PROVIDER, order = -110000)
public class EchoFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
        if (inv.getMethodName().equals(Constants.$ECHO) && inv.getArguments() != null && inv.getArguments().length == 1) return new RpcResult(
                                                                                                                                              inv.getArguments()[0]);
        return invoker.invoke(inv);
    }
}
