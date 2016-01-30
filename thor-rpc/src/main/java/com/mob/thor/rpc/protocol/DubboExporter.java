package com.mob.thor.rpc.protocol;

import java.util.Map;

import com.mob.thor.rpc.api.Exporter;
import com.mob.thor.rpc.api.Invoker;
import com.mob.thor.rpc.api.protocol.AbstractExporter;

public class DubboExporter<T> extends AbstractExporter<T> {

    private final String                   key;

    private final Map<String, Exporter<?>> exporterMap;

    public DubboExporter(Invoker<T> invoker, String key, Map<String, Exporter<?>> exporterMap) {
        super(invoker);
        this.key = key;
        this.exporterMap = exporterMap;
    }

    @Override
    public void unexport() {
        super.unexport();
        exporterMap.remove(key);
    }
}
