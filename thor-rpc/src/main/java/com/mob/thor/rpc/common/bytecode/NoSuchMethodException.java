/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.rpc.common.bytecode;

/**
 * @author zxc Dec 31, 2015 6:23:20 PM
 */
public class NoSuchMethodException extends RuntimeException {

    private static final long serialVersionUID = -2725364246023268766L;

    public NoSuchMethodException() {
        super();
    }

    public NoSuchMethodException(String msg) {
        super(msg);
    }
}
