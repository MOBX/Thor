package com.mob.thor.rpc.api.filter;

import java.util.Arrays;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;

/**
 * 如果执行timeout，则log记录下，不干涉服务的运行
 * 
 * @author zxc Jan 5, 2016 8:48:53 PM
 */
@Activate(group = Constants.PROVIDER)
public class TimeoutFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TimeoutFilter.class);

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        long elapsed = System.currentTimeMillis() - start;
        if (invoker.getUrl() != null
            && elapsed > invoker.getUrl().getMethodParameter(invocation.getMethodName(), "timeout", Integer.MAX_VALUE)) {
            if (logger.isWarnEnabled()) {
                logger.warn("invoke time out. method: " + invocation.getMethodName() + "arguments: "
                            + Arrays.toString(invocation.getArguments()) + " , url is " + invoker.getUrl()
                            + ", invoke elapsed " + elapsed + " ms.");
            }
        }
        return result;
    }
}
