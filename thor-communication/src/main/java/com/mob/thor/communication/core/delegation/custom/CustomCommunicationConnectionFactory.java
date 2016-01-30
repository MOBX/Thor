/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.custom;

import java.text.MessageFormat;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import com.mob.thor.communication.core.CommunicationEndpoint;
import com.mob.thor.communication.core.delegation.connection.CommunicationConnection;
import com.mob.thor.communication.core.delegation.connection.CommunicationConnectionFactory;
import com.mob.thor.communication.core.model.CommunicationParam;
import com.mob.thor.rpc.api.ProxyFactory;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.ExtensionLoader;
import com.mob.thor.rpc.protocol.ThorProtocol;

/**
 * custom rpc服务链接的factory
 * 
 * @author zxc Dec 24, 2015 4:41:02 PM
 */
public class CustomCommunicationConnectionFactory implements CommunicationConnectionFactory {

    private final String                       THOR_SERVICE_URL = "thor://{0}:{1}?client=netty&codec=dubbo&serialization=java&lazy=true&iothreads=4&threads=50&connections=30&acceptEvent.timeout=50000";
    private ThorProtocol                       protocol         = ThorProtocol.getDubboProtocol();

    private ProxyFactory                       proxyFactory     = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension("javassist");

    private Map<String, CommunicationEndpoint> connections      = null;

    public CustomCommunicationConnectionFactory() {
        connections = new MapMaker().makeComputingMap(new Function<String, CommunicationEndpoint>() {

            public CommunicationEndpoint apply(String serviceUrl) {
                return proxyFactory.getProxy(protocol.refer(CommunicationEndpoint.class, URL.valueOf(serviceUrl)));
            }
        });
    }

    public CommunicationConnection createConnection(CommunicationParam params) {
        if (params == null) {
            throw new IllegalArgumentException("param is null!");
        }

        // 构造对应的url
        String serviceUrl = MessageFormat.format(THOR_SERVICE_URL, params.getIp(), String.valueOf(params.getPort()));
        CommunicationEndpoint endpoint = connections.get(serviceUrl);
        return new CustomCommunicationConnection(params, endpoint);

    }

    public void releaseConnection(CommunicationConnection connection) {
        // do nothing
    }
}
