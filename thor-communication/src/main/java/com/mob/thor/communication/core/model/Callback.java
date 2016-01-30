/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.model;

/**
 * 通讯的异步callback回调接口
 * 
 * @author zxc Dec 24, 2015 4:48:31 PM
 */
public interface Callback<PARAM> {

    public void call(PARAM event);
}
