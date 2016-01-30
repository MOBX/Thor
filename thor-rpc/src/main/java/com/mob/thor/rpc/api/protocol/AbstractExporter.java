package com.mob.thor.rpc.api.protocol;

import com.mob.thor.rpc.api.Exporter;
import com.mob.thor.rpc.api.Invoker;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;

public abstract class AbstractExporter<T> implements Exporter<T> {

    protected final Logger   logger     = LoggerFactory.getLogger(getClass());

    private final Invoker<T> invoker;

    private volatile boolean unexported = false;

    public AbstractExporter(Invoker<T> invoker) {
        if (invoker == null) throw new IllegalStateException("service invoker == null");
        if (invoker.getInterface() == null) throw new IllegalStateException("service type == null");
        if (invoker.getUrl() == null) throw new IllegalStateException("service url == null");
        this.invoker = invoker;
    }

    public Invoker<T> getInvoker() {
        return invoker;
    }

    public void unexport() {
        if (unexported) {
            return;
        }
        unexported = true;
        getInvoker().destroy();
    }

    public String toString() {
        return getInvoker().toString();
    }
}
