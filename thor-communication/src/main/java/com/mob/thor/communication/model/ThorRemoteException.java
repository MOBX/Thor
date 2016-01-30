/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.model;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author zxc Dec 24, 2015 5:41:33 PM
 */
public class ThorRemoteException extends NestableRuntimeException {

    private static final long serialVersionUID = 7286951156427461822L;

    private String            errorCode;
    private String            errorDesc;

    public ThorRemoteException(String errorCode) {
        super(errorCode);
    }

    public ThorRemoteException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ThorRemoteException(String errorCode, String errorDesc) {
        super(errorCode + ":" + errorDesc);
    }

    public ThorRemoteException(String errorCode, String errorDesc, Throwable cause) {
        super(errorCode + ":" + errorDesc, cause);
    }

    public ThorRemoteException(Throwable cause) {
        super(cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
