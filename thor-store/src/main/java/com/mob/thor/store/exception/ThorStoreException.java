/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.store.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author zxc Dec 28, 2015 5:16:12 PM
 */
public class ThorStoreException extends NestableRuntimeException {

    private static final long serialVersionUID = -2020122055528858380L;

    public ThorStoreException(String errorCode) {
        super(errorCode);
    }

    public ThorStoreException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ThorStoreException(String errorCode, String errorDesc) {
        super(errorCode + ":" + errorDesc);
    }

    public ThorStoreException(String errorCode, String errorDesc, Throwable cause) {
        super(errorCode + ":" + errorDesc, cause);
    }

    public ThorStoreException(Throwable cause) {
        super(cause);
    }

    public Throwable fillInStackTrace() {
        return this;
    }
}
