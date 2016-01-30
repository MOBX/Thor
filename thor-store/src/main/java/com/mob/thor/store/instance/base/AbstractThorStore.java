/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.store.instance.base;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lamfire.utils.ArrayUtils;
import com.lamfire.utils.StringUtils;
import com.mob.thor.store.api.ThorStore;
import com.mob.thor.store.exception.ThorStoreException;
import com.mob.thor.store.support.StoreDefinition;

/**
 * @author zxc Dec 28, 2015 5:27:03 PM
 * @param <T>
 */
public abstract class AbstractThorStore<T extends Serializable> implements ThorStore<T>, StoreDefinition {

    protected static Logger logger = LoggerFactory.getLogger(AbstractThorStore.class);

    protected String        topic;

    public AbstractThorStore(String topic) {
        if (StringUtils.isEmpty(topic)) {
            throw new ThorStoreException("topic is null!");
        }
        this.topic = topic;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void put(T... data) throws InterruptedException, ThorStoreException {
        if (ArrayUtils.isEmpty(data)) {
            return;
        }
    }

    @Override
    public T get() throws ThorStoreException {
        return null;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
