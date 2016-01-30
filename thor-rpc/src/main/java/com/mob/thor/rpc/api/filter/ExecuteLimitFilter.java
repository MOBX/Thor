package com.mob.thor.rpc.api.filter;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.Activate;

@Activate(group = Constants.PROVIDER, value = Constants.EXECUTES_KEY)
public class ExecuteLimitFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = invoker.getUrl();
        String methodName = invocation.getMethodName();
        int max = url.getMethodParameter(methodName, Constants.EXECUTES_KEY, 0);
        if (max > 0) {
            RpcStatus count = RpcStatus.getStatus(url, invocation.getMethodName());
            if (count.getActive() >= max) {
                throw new RpcException("Failed to invoke method " + invocation.getMethodName() + " in provider " + url
                                       + ", cause: The service using threads greater than service executes=\"" + max
                                       + "\" limited.");
            }
        }
        long begin = System.currentTimeMillis();
        boolean isException = false;
        RpcStatus.beginCount(url, methodName);
        try {
            Result result = invoker.invoke(invocation);
            return result;
        } catch (Throwable t) {
            isException = true;
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RpcException("unexpected exception when ExecuteLimitFilter", t);
            }
        } finally {
            RpcStatus.endCount(url, methodName, System.currentTimeMillis() - begin, isException);
        }
    }

}
