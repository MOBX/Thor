/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.node;

import com.mob.thor.communication.core.CommunicationClient;
import com.mob.thor.communication.core.delegation.DefaultCommunicationClientImpl;
import com.mob.thor.communication.core.delegation.connection.CommunicationConnectionFactory;
import com.mob.thor.communication.core.delegation.custom.CustomCommunicationConnectionFactory;
import com.mob.thor.node.application.event.DataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zxc Dec 24, 2015 5:51:43 PM
 */
public class ClientTest {

    private static final Logger        logger = LoggerFactory.getLogger(ClientTest.class);

    private static CommunicationClient client = null;

    public static void main(String[] args) {
        initial();
        testRmi_createEvent();
    }

    public static void initial() {
        CommunicationConnectionFactory factory = new CustomCommunicationConnectionFactory();
        client = new DefaultCommunicationClientImpl(factory);
        client.initial();
    }

    public static void testRmi_createEvent() {
        DataEvent event = new DataEvent();
        event.setObject(new String("hello world!"));

        Object result = client.call("127.0.0.1:2088", event);
        logger.error("[call result:{}]", result);
        while (true) {
            try {
                Thread.sleep(10000);
                Object _result = client.call("127.0.0.1:2088", event);
                logger.error("[call result:{}]", _result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
