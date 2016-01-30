/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.rpc.common.beanutil;

/**
 * @author zxc Dec 31, 2015 6:21:50 PM
 */
public enum JavaBeanAccessor {

    /** Field accessor. */
    FIELD,
    /** Method accessor. */
    METHOD,
    /** Method prefer to field. */
    ALL;

    public static boolean isAccessByMethod(JavaBeanAccessor accessor) {
        return METHOD.equals(accessor) || ALL.equals(accessor);
    }

    public static boolean isAccessByField(JavaBeanAccessor accessor) {
        return FIELD.equals(accessor) || ALL.equals(accessor);
    }
}
