package com.mob.thor.rpc.api.filter;

import java.util.Map;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.utils.ConfigUtils;

@Activate(group = Constants.PROVIDER, value = Constants.TOKEN_KEY)
public class TokenFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
        String token = invoker.getUrl().getParameter(Constants.TOKEN_KEY);
        if (ConfigUtils.isNotEmpty(token)) {
            Class<?> serviceType = invoker.getInterface();
            Map<String, String> attachments = inv.getAttachments();
            String remoteToken = attachments == null ? null : attachments.get(Constants.TOKEN_KEY);
            if (!token.equals(remoteToken)) {
                throw new RpcException("Invalid token! Forbid invoke remote service " + serviceType + " method "
                                       + inv.getMethodName() + "() from consumer "
                                       + RpcContext.getContext().getRemoteHost() + " to provider "
                                       + RpcContext.getContext().getLocalHost());
            }
        }
        return invoker.invoke(inv);
    }
}
