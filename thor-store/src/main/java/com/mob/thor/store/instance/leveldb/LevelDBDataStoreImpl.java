/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.store.instance.leveldb;

import java.io.Serializable;
import java.util.NoSuchElementException;

import org.apache.commons.lang.SerializationUtils;

import com.lamfire.pandora.FireQueue;
import com.lamfire.utils.ArrayUtils;
import com.mob.thor.store.exception.ThorStoreException;
import com.mob.thor.store.instance.base.AbstractThorStore;

/**
 * 数据存储的levledb实现
 * 
 * @author zxc Dec 28, 2015 5:24:32 PM
 */
@SuppressWarnings("unchecked")
public class LevelDBDataStoreImpl<T extends Serializable> extends AbstractThorStore<T> {

    private LevelDBHelper pandora = LevelDBHelper.getInstance();

    public LevelDBDataStoreImpl(String topic) {
        super(topic);
    }

    @Override
    public synchronized void put(T... datas) throws InterruptedException, ThorStoreException {
        if (ArrayUtils.isEmpty(datas)) {
            return;
        }
        FireQueue queue = getQueue();
        if (queue == null) {
            throw new ThorStoreException("FireQueue is null!");
        }
        for (T obj : datas) {
            if (obj == null) {
                continue;
            }
            byte[] byteDta = SerializationUtils.serialize(obj);
            if (ArrayUtils.isEmpty(byteDta)) {
                continue;
            }
            queue.push(byteDta);
            this.notifyAll();
        }
    }

    @Override
    public synchronized T get() throws ThorStoreException {
        FireQueue queue = getQueue();
        if (queue == null) {
            throw new ThorStoreException("FireQueue is null!");
        }
        byte[] byteDta = null;
        do {
            try {
                byteDta = queue.pop();
            } catch (NoSuchElementException e) {
                // TODO:
            }
            if (byteDta != null) break;
            try {
                this.wait();
            } catch (InterruptedException e) {
                // TODO:
            }
        } while (byteDta == null);
        if (ArrayUtils.isEmpty(byteDta)) {
            return null;
        }
        return (T) SerializationUtils.deserialize(byteDta);
    }

    private FireQueue getQueue() {
        return pandora.getQueue(String.format(STORE_QUEUE, this.getTopic()));
    }
}
