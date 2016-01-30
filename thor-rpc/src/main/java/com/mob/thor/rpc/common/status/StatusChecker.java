package com.mob.thor.rpc.common.status;

import com.mob.thor.rpc.common.extension.SPI;

@SPI
public interface StatusChecker {

    /**
     * check status
     * 
     * @return status
     */
    Status check();

}
