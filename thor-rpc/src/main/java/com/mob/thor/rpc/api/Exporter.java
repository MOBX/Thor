package com.mob.thor.rpc.api;

/**
 * Exporter. (API/SPI, Prototype, ThreadSafe)
 * 
 * @see com.mob.thor.rpc.api.Protocol#export(Invoker)
 * @see com.mob.thor.rpc.api.ExporterListener
 * @see com.mob.thor.rpc.api.protocol.AbstractExporter
 * @author zxc
 */
public interface Exporter<T> {

    /**
     * get invoker.
     * 
     * @return invoker
     */
    Invoker<T> getInvoker();

    /**
     * unexport. <code>
     *     getInvoker().destroy();
     * </code>
     */
    void unexport();
}
