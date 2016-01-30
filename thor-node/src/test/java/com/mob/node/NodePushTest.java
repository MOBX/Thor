/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mob.thor.communication.core.registry.CommunicationRegistry;
import com.mob.thor.node.application.CommunicationAppServiceImpl;
import com.mob.thor.node.application.event.DataEventType;
import com.mob.thor.node.application.wrapper.EndPointClient;
import com.mob.thor.node.application.wrapper.EndPointServer;

/**
 * @author zxc Dec 31, 2015 4:25:38 PM
 */
public class NodePushTest {

    private static final Logger         logger = LoggerFactory.getLogger(NodePushTest.class);

    private static EndPointClient instance;

    static {
        new EndPointServer();
        CommunicationRegistry.regist(DataEventType.send, new CommunicationAppServiceImpl());
        CommunicationRegistry.regist(DataEventType.push, new CommunicationAppServiceImpl());
        CommunicationRegistry.regist(DataEventType.pull, new CommunicationAppServiceImpl());
        instance = new EndPointClient();
    }

    public static void main(String[] args) {
        testRmi_pushEvent();
    }

    public static synchronized EndPointClient getInstance() {
        return instance;
    }

    public static void testRmi_pushEvent() {
        EndPointClient endPointClient = getInstance();
        // endPointClient.send(DataEventType.push, "test", new HelloWorld("start hello world!" + UUIDGen.uuid()));

        SendSmsFootprint sendSmsFootprint = new SendSmsFootprint();
        sendSmsFootprint.setId("55dd7f061fdfea7e510db06a");
        sendSmsFootprint.setVcode("vcode");
        sendSmsFootprint.setCode("6392");
        sendSmsFootprint.setInitAt(1440579334066l);
        sendSmsFootprint.setInitState(0);
        sendSmsFootprint.setSenderDesc("senderDesc");
        sendSmsFootprint.setSenderAt(1440579334019l);
        sendSmsFootprint.setAppKey("9adfe1d476cd");
        sendSmsFootprint.setId("86");
        sendSmsFootprint.setChannles("WJXJT");
        sendSmsFootprint.setSource(0);
        sendSmsFootprint.setSmsId("55dd7f067df97e7ece051655");
        sendSmsFootprint.setPhone("15521005201");

        endPointClient.send(DataEventType.push, "test", sendSmsFootprint);

        while (true) {
            try {
                Thread.sleep(10000);
                // endPointClient.send(DataEventType.push, "test", new HelloWorld("hello world!" + UUIDGen.uuid()));
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }
}
