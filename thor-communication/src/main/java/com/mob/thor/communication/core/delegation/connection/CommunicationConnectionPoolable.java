/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.connection;

import com.mob.thor.communication.core.exception.CommunicationException;
import com.mob.thor.communication.core.model.CommunicationParam;
import com.mob.thor.communication.core.model.Event;

/**
 * 可被链接池管理的对象, @see {@linkplain CommunicationConnectionPoolableFactory}
 * 
 * @author zxc Dec 24, 2015 4:37:27 PM
 */
public class CommunicationConnectionPoolable implements CommunicationConnection {

    private CommunicationConnectionPoolFactory pool;
    private CommunicationConnection            delegate;

    public CommunicationConnectionPoolable(CommunicationConnection connection, CommunicationConnectionPoolFactory pool) {
        this.delegate = connection;
        this.pool = pool;
    }

    public Object call(Event event) {
        return getDelegate().call(event);
    }

    public void close() throws CommunicationException {
        pool.releaseConnection(this);
    }

    public CommunicationParam getParams() {
        return getDelegate().getParams();
    }

    /**
     * @return 返回原始connection对象
     */
    public CommunicationConnection getDelegate() {
        return this.delegate;
    }
}
