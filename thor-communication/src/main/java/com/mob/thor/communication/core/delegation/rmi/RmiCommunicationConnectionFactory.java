/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.rmi;

import com.mob.thor.communication.core.delegation.connection.CommunicationConnection;
import com.mob.thor.communication.core.delegation.connection.CommunicationConnectionFactory;
import com.mob.thor.communication.core.model.CommunicationParam;

/**
 * 基于rmi的通讯链接实现
 * 
 * @author zxc Dec 24, 2015 4:41:14 PM
 */
public class RmiCommunicationConnectionFactory implements CommunicationConnectionFactory {

    static {
        // 初始化rmi相关的参数
        System.setProperty("sun.rmi.transport.connectTimeout", "30000"); // 连接超时
    }

    // private final String RMI_SERVICE_URL = "rmi://{0}:{1}/endpoint";

    @Override
    public CommunicationConnection createConnection(CommunicationParam params) {
        if (params == null) {
            throw new IllegalArgumentException("param is null!");
        }

        // 构造对应的url
        // String serviceUrl = MessageFormat.format(RMI_SERVICE_URL, params.getIp(), String.valueOf(params.getPort()));
        // 自己实现的有连接池的Stub
        // RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
        // proxy.setServiceUrl(serviceUrl);
        // proxy.setServiceInterface(CommunicationEndpoint.class);
        // proxy.afterPropertiesSet();
        // return new RmiCommunicationConnection(params, (CommunicationEndpoint) proxy.getObject());// 创建链接
        return null;
    }

    @Override
    public void releaseConnection(CommunicationConnection connection) {
        // do nothing
    }
}
