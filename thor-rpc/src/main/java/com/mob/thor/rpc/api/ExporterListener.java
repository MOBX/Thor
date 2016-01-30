package com.mob.thor.rpc.api;

import com.mob.thor.rpc.common.extension.SPI;

/**
 * ExporterListener. (SPI, Singleton, ThreadSafe)
 * 
 * @author zxc Jan 4, 2016 1:31:40 PM
 */
@SPI
public interface ExporterListener {

    /**
     * The exporter exported.
     * 
     * @see com.mob.thor.rpc.Protocol#export(Invoker)
     * @param exporter
     * @throws RpcException
     */
    void exported(Exporter<?> exporter) throws RpcException;

    /**
     * The exporter unexported.
     * 
     * @see com.mob.thor.rpc.Exporter#unexport()
     * @param exporter
     * @throws RpcException
     */
    void unexported(Exporter<?> exporter);
}
