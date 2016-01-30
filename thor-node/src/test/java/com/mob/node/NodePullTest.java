/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mob.thor.node.application.event.DataEventType;
import com.mob.thor.node.application.wrapper.EndPointClient;

/**
 * @author zxc Dec 31, 2015 4:35:10 PM
 */
public class NodePullTest {

    private static final Logger         logger   = LoggerFactory.getLogger(NodePushTest.class);

    private static final EndPointClient instance = new EndPointClient();

    public static void main(String[] args) {
        testRmi_pullEvent();
    }

    public static synchronized EndPointClient getInstance() {
        return instance;
    }

    public static void testRmi_pullEvent() {
        EndPointClient endPointClient = getInstance();
        Object object = endPointClient.send(DataEventType.pull, "test", null);
        logger.error("" + object);

        while (true) {
            try {
                Thread.sleep(10000);
                Object _object = endPointClient.send(DataEventType.pull, "test", null);
                logger.error("" + _object);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }
}
