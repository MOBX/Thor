package com.mob.thor.rpc.api.listener;

import java.util.List;

import com.mob.thor.rpc.api.Exporter;
import com.mob.thor.rpc.api.ExporterListener;
import com.mob.thor.rpc.api.Invoker;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;

public class ListenerExporterWrapper<T> implements Exporter<T> {

    private static final Logger          logger = LoggerFactory.getLogger(ListenerExporterWrapper.class);

    private final Exporter<T>            exporter;

    private final List<ExporterListener> listeners;

    public ListenerExporterWrapper(Exporter<T> exporter, List<ExporterListener> listeners) {
        if (exporter == null) {
            throw new IllegalArgumentException("exporter == null");
        }
        this.exporter = exporter;
        this.listeners = listeners;
        if (listeners != null && listeners.size() > 0) {
            RuntimeException exception = null;
            for (ExporterListener listener : listeners) {
                if (listener != null) {
                    try {
                        listener.exported(this);
                    } catch (RuntimeException t) {
                        logger.error(t.getMessage(), t);
                        exception = t;
                    }
                }
            }
            if (exception != null) {
                throw exception;
            }
        }
    }

    public Invoker<T> getInvoker() {
        return exporter.getInvoker();
    }

    public void unexport() {
        try {
            exporter.unexport();
        } finally {
            if (listeners != null && listeners.size() > 0) {
                RuntimeException exception = null;
                for (ExporterListener listener : listeners) {
                    if (listener != null) {
                        try {
                            listener.unexported(this);
                        } catch (RuntimeException t) {
                            logger.error(t.getMessage(), t);
                            exception = t;
                        }
                    }
                }
                if (exception != null) {
                    throw exception;
                }
            }
        }
    }
}
