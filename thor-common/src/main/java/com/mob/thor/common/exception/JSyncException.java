/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author zxc Dec 1, 2015 5:54:56 PM
 */
public class JSyncException extends NestableRuntimeException {

    private static final long serialVersionUID = -854893533794556357L;

    public JSyncException(String errorCode) {
        super(errorCode);
    }

    public JSyncException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public JSyncException(String errorCode, String errorDesc) {
        super(errorCode + ":" + errorDesc);
    }

    public JSyncException(String errorCode, String errorDesc, Throwable cause) {
        super(errorCode + ":" + errorDesc, cause);
    }

    public JSyncException(Throwable cause) {
        super(cause);
    }

    public Throwable fillInStackTrace() {
        return this;
    }
}