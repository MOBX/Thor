/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.rpc.common.bytecode;

/**
 * @author zxc Dec 31, 2015 6:23:34 PM
 */
public class NoSuchPropertyException extends RuntimeException {

    private static final long serialVersionUID = -2725364246023268766L;

    public NoSuchPropertyException() {
        super();
    }

    public NoSuchPropertyException(String msg) {
        super(msg);
    }
}
