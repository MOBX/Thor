/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.store.api;

import com.mob.thor.store.exception.ThorStoreException;

/**
 * 数据清除接口
 * 
 * @author zxc Dec 28, 2015 5:07:03 PM
 */
public interface ThorStoreScavenge {

    /**
     * 删除所有的数据
     */
    void cleanAll() throws ThorStoreException;
}
