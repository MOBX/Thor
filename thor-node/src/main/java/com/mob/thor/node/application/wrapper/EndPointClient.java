/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.node.application.wrapper;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lamfire.utils.PropertiesUtils;
import com.mob.thor.common.utils.Assert;
import com.mob.thor.communication.core.CommunicationClient;
import com.mob.thor.communication.core.delegation.DefaultCommunicationClientImpl;
import com.mob.thor.communication.core.delegation.connection.CommunicationConnectionFactory;
import com.mob.thor.communication.core.delegation.custom.CustomCommunicationConnectionFactory;
import com.mob.thor.communication.core.model.HeartEvent;
import com.mob.thor.node.application.convert.EventConvert;
import com.mob.thor.node.application.event.DataEvent;
import com.mob.thor.node.application.event.DataEventType;

/**
 * @author zxc Dec 25, 2015 11:03:38 AM
 */
public class EndPointClient {

    private static final Logger        logger = LoggerFactory.getLogger(EndPointClient.class);

    private static CommunicationClient client = null;

    private static String              remote_address;

    private static Properties          pro;

    static {
        pro = PropertiesUtils.load("communication.properties", EndPointClient.class);
        remote_address = pro.getProperty("remote.address");
    }

    public EndPointClient() {
        initial();
    }

    public void shutdown() {
        if (client != null) {
            client.destory();
        }
    }

    public void initial() {
        CommunicationConnectionFactory factory = new CustomCommunicationConnectionFactory();
        client = new DefaultCommunicationClientImpl(factory);
        client.initial();
        if (client == null) {
            logger.error("client.initial() error!");
            return;
        }
        client.call(remote_address, new HeartEvent());
    }

    public void subscribe(String topic, ExecutorService threadPool, Runnable exec) {
        Assert.notNull(topic, "topic is null");
        Assert.notNull(exec, "runnable is null");
        Assert.notNull(threadPool, "threadPool is null");
        while (true) {
            Object obj = this.send(DataEventType.pull, topic, null);
            if (obj == null) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (obj instanceof String) {
                logger.error("object is String,no support!msg=" + obj);
            } else {
                EventConvert.fixdComplexObject(exec, obj);
                threadPool.submit(exec);
            }
        }
    }

    public Object send(Object... obj) {
        if (obj == null || obj.length == 0) {
            return null;
        }
        if (obj.length == 2) {
            return client.call(remote_address, new DataEvent((String) obj[0], obj[1]));
        }
        if (obj.length == 3) {
            return client.call(remote_address, new DataEvent((DataEventType) obj[0], (String) obj[1], obj[2]));
        }
        return null;
    }
}
