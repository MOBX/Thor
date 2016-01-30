package com.mob.thor.rpc.api.listener;

import com.mob.thor.rpc.api.Exporter;
import com.mob.thor.rpc.api.ExporterListener;
import com.mob.thor.rpc.api.RpcException;

public abstract class ExporterListenerAdapter implements ExporterListener {

    public void exported(Exporter<?> exporter) throws RpcException {
    }

    public void unexported(Exporter<?> exporter) throws RpcException {
    }
}
