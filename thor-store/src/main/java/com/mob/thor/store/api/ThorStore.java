/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.store.api;

import java.io.Serializable;

import com.mob.thor.store.exception.ThorStoreException;

/**
 * 数据贮存接口
 * 
 * @author zxc Dec 28, 2015 5:06:47 PM
 */
public interface ThorStore<T extends Serializable> {

    /**
     * 添加一组数据对象，阻塞等待其操作完成
     */
    @SuppressWarnings("unchecked")
    void put(T... data) throws InterruptedException, ThorStoreException;

    /**
     * 取一组最新的数据
     * 
     * @return
     * @throws ThorStoreException
     */
    T get() throws ThorStoreException;
}
