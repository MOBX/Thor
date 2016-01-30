/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author zxc Dec 24, 2015 3:45:00 PM
 */
public class CommunicationException extends NestableRuntimeException {

    private static final long serialVersionUID = 4834411194090937775L;

    private String            errorCode;
    private String            errorDesc;

    public CommunicationException(String errorCode) {
        super(errorCode);
    }

    public CommunicationException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public CommunicationException(String errorCode, String errorDesc) {
        super(errorCode + ":" + errorDesc);
    }

    public CommunicationException(String errorCode, String errorDesc, Throwable cause) {
        super(errorCode + ":" + errorDesc, cause);
    }

    public CommunicationException(Throwable cause) {
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
