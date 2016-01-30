/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.connection;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.lamfire.pool.PoolableObjectFactory;
import com.mob.thor.communication.core.model.CommunicationParam;
import com.mob.thor.communication.core.model.HeartEvent;

/**
 * 链接池内容管理工厂, @see {@linkplain GenericObjectPool} , {@linkplain PoolableObjectFactory}
 * 
 * @author zxc Dec 24, 2015 4:37:27 PM
 */
public class CommunicationConnectionPoolableFactory implements KeyedPoolableObjectFactory {

    private CommunicationConnectionFactory factory;

    public CommunicationConnectionPoolableFactory(CommunicationConnectionFactory factory) {
        this.factory = factory;
    }

    public void destroyObject(Object key, Object obj) throws Exception {
        if (obj instanceof CommunicationConnectionPoolable) {
            factory.releaseConnection(((CommunicationConnectionPoolable) obj).getDelegate());// 关闭原始链接
        } else {
            throw new IllegalArgumentException("pool object is not CommunicationConnectionPoolable!");
        }
    }

    public Object makeObject(Object key) throws Exception {
        if (key instanceof CommunicationParam) {
            return factory.createConnection((CommunicationParam) key);// 创建链接
        } else {
            throw new IllegalArgumentException("key object is not CommunicationParams!");
        }
    }

    public void passivateObject(Object key, Object obj) throws Exception {
    }

    public void activateObject(Object key, Object obj) throws Exception {
    }

    public boolean validateObject(Object key, Object obj) {
        if (obj instanceof CommunicationConnectionPoolable) {
            CommunicationConnectionPoolable connection = (CommunicationConnectionPoolable) obj;
            try {
                Object value = connection.call(new HeartEvent());// 发起一次心跳检查
                return value != null;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
