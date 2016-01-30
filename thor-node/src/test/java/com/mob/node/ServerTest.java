/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.node;

import java.util.concurrent.Executors;

import com.mob.thor.communication.core.delegation.custom.CustomCommunicationEndpoint;
import com.mob.thor.communication.core.registry.CommunicationRegistry;
import com.mob.thor.node.application.CommunicationAppServiceImpl;
import com.mob.thor.node.application.event.DataEventType;

/**
 * @author zxc Dec 24, 2015 5:52:02 PM
 */
public class ServerTest {

    public static void initial() {
        CustomCommunicationEndpoint endpoint = new CustomCommunicationEndpoint("127.0.0.1", 2088);
        endpoint.initial();
    }

    public static void main(String[] args) throws Exception {
        initial();
        CommunicationRegistry.regist(DataEventType.send,
                                     new CommunicationAppServiceImpl(Executors.newFixedThreadPool(2), new Runnable() {

                                         private String name;

                                         @SuppressWarnings("unused")
                                         public String getName() {
                                             return name;
                                         }

                                         @SuppressWarnings("unused")
                                         public void setName(String name) {
                                             this.name = name;
                                         }

                                         @Override
                                         public void run() {
                                             System.out.println(name);
                                         }
                                     }));
        while (true) {
            Thread.sleep(1000);
        }
    }
}
