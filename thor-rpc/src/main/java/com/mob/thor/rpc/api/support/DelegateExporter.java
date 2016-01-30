package com.mob.thor.rpc.api.support;

import com.mob.thor.rpc.api.Exporter;
import com.mob.thor.rpc.api.Invoker;

public class DelegateExporter<T> implements Exporter<T> {

    private final Exporter<T> exporter;

    public DelegateExporter(Exporter<T> exporter) {
        if (exporter == null) {
            throw new IllegalArgumentException("exporter can not be null");
        } else {
            this.exporter = exporter;
        }

    }

    public Invoker<T> getInvoker() {
        return exporter.getInvoker();
    }

    public void unexport() {
        exporter.unexport();
    }
}
