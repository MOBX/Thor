package com.mob.thor.rpc.api.proxy;

import com.mob.thor.rpc.api.Invoker;
import com.mob.thor.rpc.api.ProxyFactory;
import com.mob.thor.rpc.api.RpcException;
import com.mob.thor.rpc.api.service.EchoService;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.utils.ReflectUtils;

public abstract class AbstractProxyFactory implements ProxyFactory {

    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        Class<?>[] interfaces = null;
        String config = invoker.getUrl().getParameter("interfaces");
        if (config != null && config.length() > 0) {
            String[] types = Constants.COMMA_SPLIT_PATTERN.split(config);
            if (types != null && types.length > 0) {
                interfaces = new Class<?>[types.length + 2];
                interfaces[0] = invoker.getInterface();
                interfaces[1] = EchoService.class;
                for (int i = 0; i < types.length; i++) {
                    interfaces[i + 1] = ReflectUtils.forName(types[i]);
                }
            }
        }
        if (interfaces == null) {
            interfaces = new Class<?>[] { invoker.getInterface(), EchoService.class };
        }
        return getProxy(invoker, interfaces);
    }

    public abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] types);
}
