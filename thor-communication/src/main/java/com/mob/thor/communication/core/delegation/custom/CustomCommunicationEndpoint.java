/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.custom;

import java.text.MessageFormat;

import com.mob.thor.communication.core.CommunicationEndpoint;
import com.mob.thor.communication.core.delegation.AbstractCommunicationEndpoint;
import com.mob.thor.rpc.api.Exporter;
import com.mob.thor.rpc.api.ProxyFactory;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.ExtensionLoader;
import com.mob.thor.rpc.protocol.ThorProtocol;

/**
 * custom的endpoint实现,仅仅使用了底层的rpc通信工具
 * 
 * @author zxc Dec 24, 2015 4:41:14 PM
 */
public class CustomCommunicationEndpoint extends AbstractCommunicationEndpoint {

    private static final String             THOR_SERVICE_URL = "thor://{0}:{1}?server=netty&codec=dubbo&serialization=java&heartbeat=5000&iothreads=4&threads=50&connections=30";
    private ThorProtocol                    protocol         = ThorProtocol.getDubboProtocol();

    private ProxyFactory                    proxyFactory     = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension("javassist");

    private Exporter<CommunicationEndpoint> exporter         = null;
    private String                          url              = "127.0.0.1";
    private int                             port             = 2088;

    public CustomCommunicationEndpoint() {

    }

    public CustomCommunicationEndpoint(String url, int port) {
        this.port = port;
        this.url = url;
    }

    public CustomCommunicationEndpoint(int port) {
        this.port = port;
    }

    public void initial() {
        String _url = MessageFormat.format(THOR_SERVICE_URL, url, String.valueOf(port));
        exporter = protocol.export(proxyFactory.getInvoker(this, CommunicationEndpoint.class, URL.valueOf(_url)));
    }

    public void destory() {
        exporter.unexport();
    }

    public void setPort(int port) {
        this.port = port;
    }
}
