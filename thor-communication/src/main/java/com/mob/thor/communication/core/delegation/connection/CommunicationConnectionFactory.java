/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.connection;

import com.mob.thor.communication.core.model.CommunicationParam;

/**
 * {@linkplain CommunicationConnection}链接创建和关闭工厂
 * 
 * @author zxc Dec 24, 2015 4:37:27 PM
 */
public interface CommunicationConnectionFactory {

    CommunicationConnection createConnection(CommunicationParam params);

    void releaseConnection(CommunicationConnection connection);
}
