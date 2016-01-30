package com.mob.thor.rpc.api.support;

import com.mob.thor.rpc.api.Exporter;
import com.mob.thor.rpc.api.Invoker;
import com.mob.thor.rpc.api.RpcException;
import com.mob.thor.rpc.api.protocol.AbstractProtocol;
import com.mob.thor.rpc.common.URL;

/**
 * MockProtocol 用于在consumer side 通过url及类型生成一个mockInvoker
 * 
 * @author zxc
 */
final public class MockProtocol extends AbstractProtocol {

    public int getDefaultPort() {
        return 0;
    }

    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        throw new UnsupportedOperationException();
    }

    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        return new MockInvoker<T>(url);
    }
}
