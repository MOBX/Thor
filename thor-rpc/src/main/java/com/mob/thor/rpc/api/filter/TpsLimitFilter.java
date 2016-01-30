package com.mob.thor.rpc.api.filter;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.api.filter.tps.DefaultTPSLimiter;
import com.mob.thor.rpc.api.filter.tps.TPSLimiter;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Activate;

/**
 * 限制 service 或方法的 tps.
 *
 * @author zxc Jan 5, 2016 8:49:29 PM
 */
@Activate(group = Constants.PROVIDER, value = Constants.TPS_LIMIT_RATE_KEY)
public class TpsLimitFilter implements Filter {

    private final TPSLimiter tpsLimiter = new DefaultTPSLimiter();

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        if (!tpsLimiter.isAllowable(invoker.getUrl(), invocation)) {
            throw new RpcException(
                                   new StringBuilder(64).append("Failed to invoke service ").append(invoker.getInterface().getName()).append(".").append(invocation.getMethodName()).append(" because exceed max service tps.").toString());
        }
        return invoker.invoke(invocation);
    }
}
