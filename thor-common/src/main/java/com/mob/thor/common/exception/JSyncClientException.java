/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author zxc Dec 1, 2015 5:58:10 PM
 */
public class JSyncClientException extends NestableRuntimeException {

    private static final long serialVersionUID = -1453302323606999766L;

    public JSyncClientException(String errorCode) {
        super(errorCode);
    }

    public JSyncClientException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public JSyncClientException(String errorCode, String errorDesc) {
        super(errorCode + ":" + errorDesc);
    }

    public JSyncClientException(String errorCode, String errorDesc, Throwable cause) {
        super(errorCode + ":" + errorDesc, cause);
    }

    public JSyncClientException(Throwable cause) {
        super(cause);
    }
}
