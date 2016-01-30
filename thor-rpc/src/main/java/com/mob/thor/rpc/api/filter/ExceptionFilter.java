package com.mob.thor.rpc.api.filter;

import java.lang.reflect.Method;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.api.service.GenericService;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;
import com.mob.thor.rpc.common.utils.ReflectUtils;
import com.mob.thor.rpc.common.utils.StringUtils;

/**
 * ExceptionInvokerFilter
 * <p>
 * 功能：
 * <ol>
 * <li>不期望的异常打ERROR日志（Provider端）<br>
 * 不期望的日志即是，没有的接口上声明的Unchecked异常。
 * <li>异常不在API包中，则Wrap一层RuntimeException。<br>
 * RPC对于第一层异常会直接序列化传输(Cause异常会String化)，避免异常在Client出不能反序列化问题。
 * </ol>
 * 
 * @author zxc Jan 5, 2016 8:47:40 PM
 */
@Activate(group = Constants.PROVIDER)
public class ExceptionFilter implements Filter {

    private final Logger logger;

    public ExceptionFilter() {
        this(LoggerFactory.getLogger(ExceptionFilter.class));
    }

    public ExceptionFilter(Logger logger) {
        this.logger = logger;
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Result result = invoker.invoke(invocation);
            if (result.hasException() && GenericService.class != invoker.getInterface()) {
                try {
                    Throwable exception = result.getException();

                    // 如果是checked异常，直接抛出
                    if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
                        return result;
                    }
                    // 在方法签名上有声明，直接抛出
                    try {
                        Method method = invoker.getInterface().getMethod(invocation.getMethodName(),
                                                                         invocation.getParameterTypes());
                        Class<?>[] exceptionClassses = method.getExceptionTypes();
                        for (Class<?> exceptionClass : exceptionClassses) {
                            if (exception.getClass().equals(exceptionClass)) {
                                return result;
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        return result;
                    }

                    // 未在方法签名上定义的异常，在服务器端打印ERROR日志
                    logger.error("Got unchecked and undeclared exception which called by "
                                         + RpcContext.getContext().getRemoteHost() + ". service: "
                                         + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                                         + ", exception: " + exception.getClass().getName() + ": "
                                         + exception.getMessage(),
                                 exception);

                    // 异常类和接口类在同一jar包里，直接抛出
                    String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                    String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                    if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
                        return result;
                    }
                    // 是JDK自带的异常，直接抛出
                    String className = exception.getClass().getName();
                    if (className.startsWith("java.") || className.startsWith("javax.")) {
                        return result;
                    }
                    // 是Thor本身的异常，直接抛出
                    if (exception instanceof RpcException) {
                        return result;
                    }

                    // 否则，包装成RuntimeException抛给客户端
                    return new RpcResult(new RuntimeException(StringUtils.toString(exception)));
                } catch (Throwable e) {
                    logger.warn("Fail to ExceptionFilter when called by " + RpcContext.getContext().getRemoteHost()
                                        + ". service: " + invoker.getInterface().getName() + ", method: "
                                        + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": "
                                        + e.getMessage(), e);
                    return result;
                }
            }
            return result;
        } catch (RuntimeException e) {
            logger.error("Got unchecked and undeclared exception which called by "
                         + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName()
                         + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": "
                         + e.getMessage(), e);
            throw e;
        }
    }

}
