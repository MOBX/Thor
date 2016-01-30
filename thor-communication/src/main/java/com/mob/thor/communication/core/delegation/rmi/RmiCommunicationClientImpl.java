/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.rmi;

import com.mob.thor.communication.core.CommunicationClient;
import com.mob.thor.communication.core.delegation.DefaultCommunicationClientImpl;
import com.mob.thor.communication.core.delegation.connection.CommunicationConnectionFactory;
import com.mob.thor.communication.core.delegation.connection.CommunicationConnectionPoolFactory;

/**
 * 基于rmi的通讯实现
 * 
 * @author zxc Dec 24, 2015 4:41:14 PM
 */
public class RmiCommunicationClientImpl extends DefaultCommunicationClientImpl implements CommunicationClient {

    // 是否使用链接池
    private boolean poolable = true;

    public void initial() {
        CommunicationConnectionFactory factory = null;
        if (poolable) {
            factory = new CommunicationConnectionPoolFactory(new RmiCommunicationConnectionFactory());
            ((CommunicationConnectionPoolFactory) factory).initial();
        } else {
            factory = new RmiCommunicationConnectionFactory();
        }

        super.setFactory(factory);
    }

    public void setPoolable(boolean poolable) {
        this.poolable = poolable;
    }
}
