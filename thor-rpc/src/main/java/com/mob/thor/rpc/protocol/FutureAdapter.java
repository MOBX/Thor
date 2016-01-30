package com.mob.thor.rpc.protocol;

import java.util.concurrent.*;

import com.mob.thor.rpc.api.Result;
import com.mob.thor.rpc.api.RpcException;
import com.mob.thor.rpc.common.utils.StringUtils;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.exchange.ResponseFuture;

public class FutureAdapter<V> implements Future<V> {

    private final ResponseFuture future;

    public FutureAdapter(ResponseFuture future) {
        this.future = future;
    }

    public ResponseFuture getFuture() {
        return future;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return future.isDone();
    }

    @SuppressWarnings("unchecked")
    public V get() throws InterruptedException, ExecutionException {
        try {
            return (V) (((Result) future.get()).recreate());
        } catch (RemotingException e) {
            throw new ExecutionException(e.getMessage(), e);
        } catch (Throwable e) {
            throw new RpcException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        int timeoutInMillis = (int) unit.convert(timeout, TimeUnit.MILLISECONDS);
        try {
            return (V) (((Result) future.get(timeoutInMillis)).recreate());
        } catch (TimeoutException e) {
            throw new TimeoutException(StringUtils.toString(e));
        } catch (RemotingException e) {
            throw new ExecutionException(e.getMessage(), e);
        } catch (Throwable e) {
            throw new RpcException(e);
        }
    }
}
