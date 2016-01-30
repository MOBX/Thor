package com.mob.thor.rpc.api.filter;

import java.util.HashMap;
import java.util.Map;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Activate;

@Activate(group = Constants.PROVIDER, order = -10000)
public class ContextFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, String> attachments = invocation.getAttachments();
        if (attachments != null) {
            attachments = new HashMap<String, String>(attachments);
            attachments.remove(Constants.PATH_KEY);
            attachments.remove(Constants.GROUP_KEY);
            attachments.remove(Constants.VERSION_KEY);
            attachments.remove(Constants.DUBBO_VERSION_KEY);
            attachments.remove(Constants.TOKEN_KEY);
            attachments.remove(Constants.TIMEOUT_KEY);
        }
        RpcContext.getContext().setInvoker(invoker).setInvocation(invocation).setAttachments(attachments).setLocalAddress(invoker.getUrl().getHost(),
                                                                                                                          invoker.getUrl().getPort());
        if (invocation instanceof RpcInvocation) {
            ((RpcInvocation) invocation).setInvoker(invoker);
        }
        try {
            return invoker.invoke(invocation);
        } finally {
            RpcContext.removeContext();
        }
    }
}
