/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.node.application.exception;

/**
 * @author zxc Dec 25, 2015 2:13:02 PM
 */
public class NodeHandleException extends RuntimeException {

    private static final long serialVersionUID = 2950003583610604528L;

    public NodeHandleException(String msg) {
        super(msg);
    }

    public NodeHandleException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
