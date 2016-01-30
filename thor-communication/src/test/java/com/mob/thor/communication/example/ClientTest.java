/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mob.thor.communication.app.event.AppCreateEvent;
import com.mob.thor.communication.core.CommunicationClient;
import com.mob.thor.communication.core.delegation.DefaultCommunicationClientImpl;
import com.mob.thor.communication.core.delegation.connection.CommunicationConnectionFactory;
import com.mob.thor.communication.core.delegation.custom.CustomCommunicationConnectionFactory;

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
        AppCreateEvent event = new AppCreateEvent();
        event.setName("rmiEvent");
        event.setIntValue(1);
        event.setBoolValue(false);
        event.setFloatValue(1.0f);
        event.setDoubleValue(1.0d);
        event.setLongValue(1l);
        event.setCharValue('a');
        event.setShortValue((short) 1);
        event.setByteValue((byte) 1);
        event.setIntegerValue(new Integer("1"));
        event.setBoolObjValue(new Boolean("false"));
        event.setFloatObjValue(new Float("1.0"));
        event.setDoubleObjValue(new Double("1.0"));
        event.setLongObjValue(new Long("1"));
        event.setCharacterValue('a');
        event.setShortObjValue(new Short("1"));
        event.setByteObjValue(new Byte("1"));

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
