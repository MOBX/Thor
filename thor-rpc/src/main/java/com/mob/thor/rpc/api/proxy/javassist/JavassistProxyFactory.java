package com.mob.thor.rpc.api.proxy.javassist;

import com.mob.thor.rpc.api.Invoker;
import com.mob.thor.rpc.api.proxy.AbstractProxyFactory;
import com.mob.thor.rpc.api.proxy.AbstractProxyInvoker;
import com.mob.thor.rpc.api.proxy.InvokerInvocationHandler;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.bytecode.Proxy;
import com.mob.thor.rpc.common.bytecode.Wrapper;

public class JavassistProxyFactory extends AbstractProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        return (T) Proxy.getProxy(interfaces).newInstance(new InvokerInvocationHandler(invoker));
    }

    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        // TODO Wrapper类不能正确处理带$的类名
        final Wrapper wrapper = Wrapper.getWrapper(proxy.getClass().getName().indexOf('$') < 0 ? proxy.getClass() : type);
        return new AbstractProxyInvoker<T>(proxy, type, url) {

            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments)
                                                                                                                throws Throwable {
                return wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
            }
        };
    }
}
