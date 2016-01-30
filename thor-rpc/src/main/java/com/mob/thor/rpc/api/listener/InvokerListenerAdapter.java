package com.mob.thor.rpc.api.listener;

import com.mob.thor.rpc.api.Invoker;
import com.mob.thor.rpc.api.InvokerListener;
import com.mob.thor.rpc.api.RpcException;

public abstract class InvokerListenerAdapter implements InvokerListener {

    public void referred(Invoker<?> invoker) throws RpcException {
    }

    public void destroyed(Invoker<?> invoker) {
    }
}
