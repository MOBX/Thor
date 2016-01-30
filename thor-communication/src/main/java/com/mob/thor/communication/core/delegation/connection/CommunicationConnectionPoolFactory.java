/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.connection;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;

import com.mob.thor.communication.core.delegation.rmi.RmiCommunicationConnectionFactory;
import com.mob.thor.communication.core.exception.CommunicationException;
import com.mob.thor.communication.core.model.CommunicationParam;

/**
 * @author zxc Dec 24, 2015 4:37:20 PM
 */
public class CommunicationConnectionPoolFactory implements CommunicationConnectionFactory {

    private volatile GenericKeyedObjectPool pool      = null;
    private CommunicationConnectionFactory  factory   = new RmiCommunicationConnectionFactory();
    private int                             maxActive = 10;

    public CommunicationConnectionPoolFactory() {
    }

    public CommunicationConnectionPoolFactory(CommunicationConnectionFactory factory) {
        this.factory = factory;
        initial();
    }

    public void initial() {
        if (factory == null) {
            throw new IllegalArgumentException("factory is null!");
        }

        // 创建链接池对象
        pool = new GenericKeyedObjectPool();
        pool.setMaxActive(maxActive);
        pool.setMaxIdle(maxActive);
        pool.setMinIdle(0);
        pool.setMaxWait(60 * 1000); // 60s
        pool.setTestOnBorrow(false);
        pool.setTestOnReturn(false);
        pool.setTimeBetweenEvictionRunsMillis(10 * 1000);
        pool.setNumTestsPerEvictionRun(maxActive * 2);
        pool.setMinEvictableIdleTimeMillis(30 * 60 * 1000);
        pool.setTestWhileIdle(true);
        pool.setFactory(new CommunicationConnectionPoolableFactory(factory)); // 设置连接池管理对象
    }

    public void destory() {
        try {
            pool.close();
        } catch (Exception e) {
            throw new CommunicationException("Connection_Pool_Close_Error", e);
        }
    }

    public CommunicationConnection createConnection(CommunicationParam params) {
        try {
            CommunicationConnectionPoolable poolable = new CommunicationConnectionPoolable(
                                                                                           (CommunicationConnection) pool.borrowObject(params),
                                                                                           this);
            return poolable;
        } catch (Exception e) {
            throw new CommunicationException("createConnection_error", e);
        }
    }

    public void releaseConnection(CommunicationConnection connection) {
        try {
            pool.returnObject(connection.getParams(), connection);
        } catch (Exception e) {
            throw new CommunicationException("releaseConnection_error", e);
        }
    }

    public void setFactory(CommunicationConnectionFactory factory) {
        this.factory = factory;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }
}
