package com.mob.thor.rpc.remoting.api.exchange.support;

import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.exchange.ResponseCallback;
import com.mob.thor.rpc.remoting.api.exchange.ResponseFuture;

public class SimpleFuture implements ResponseFuture {

    private final Object value;

    public SimpleFuture(Object value) {
        this.value = value;
    }

    public Object get() throws RemotingException {
        return value;
    }

    public Object get(int timeoutInMillis) throws RemotingException {
        return value;
    }

    public void setCallback(ResponseCallback callback) {
        callback.done(value);
    }

    public boolean isDone() {
        return true;
    }

}
