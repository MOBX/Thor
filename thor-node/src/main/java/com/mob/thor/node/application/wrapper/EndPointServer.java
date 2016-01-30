/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.node.application.wrapper;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

import com.lamfire.utils.PropertiesUtils;
import com.lamfire.utils.StringUtils;
import com.mob.thor.common.utils.Assert;
import com.mob.thor.communication.core.delegation.custom.CustomCommunicationEndpoint;
import com.mob.thor.communication.core.registry.CommunicationRegistry;
import com.mob.thor.node.application.CommunicationAppServiceImpl;
import com.mob.thor.node.application.event.DataEventType;

/**
 * @author zxc Dec 25, 2015 11:03:54 AM
 */
public class EndPointServer {

    private static String               local_address;
    private static int                  local_port;

    private CustomCommunicationEndpoint endpoint;

    private static Properties           pro;

    static {
        pro = PropertiesUtils.load("communication.properties", EndPointClient.class);
        String url = pro.getProperty("local.address");
        String[] strArray = StringUtils.split(url, ":");
        local_address = strArray[0];
        local_port = Integer.valueOf(strArray[1]);
    }

    public EndPointServer() {
        initial();
    }

    public void start(ExecutorService threadPool, Runnable runnable) {
        Assert.notNull(runnable, "runnable is null");
        Assert.notNull(threadPool, "threadPool is null");
        CommunicationRegistry.regist(DataEventType.send, new CommunicationAppServiceImpl(threadPool, runnable));
    }

    public void shutdown() {
        if (endpoint != null) {
            endpoint.destory();
        }
    }

    public void initial() {
        CustomCommunicationEndpoint endpoint = new CustomCommunicationEndpoint(local_address, local_port);
        endpoint.initial();
    }
}
