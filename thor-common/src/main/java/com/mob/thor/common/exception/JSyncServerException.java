/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.exception;

/**
 * @author zxc Dec 1, 2015 5:54:07 PM
 */
public class JSyncServerException extends JSyncException {

    private static final long serialVersionUID = 8494282240233242644L;

    public JSyncServerException(String errorCode) {
        super(errorCode);
    }

    public JSyncServerException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public JSyncServerException(String errorCode, String errorDesc) {
        super(errorCode + ":" + errorDesc);
    }

    public JSyncServerException(String errorCode, String errorDesc, Throwable cause) {
        super(errorCode + ":" + errorDesc, cause);
    }

    public JSyncServerException(Throwable cause) {
        super(cause);
    }
}
