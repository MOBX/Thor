/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.example;

import com.mob.thor.communication.app.CommunicationAppService;
import com.mob.thor.communication.app.CommunicationAppServiceImpl;
import com.mob.thor.communication.app.event.AppEventType;
import com.mob.thor.communication.core.delegation.custom.CustomCommunicationEndpoint;
import com.mob.thor.communication.core.registry.CommunicationRegistry;

/**
 * @author zxc Dec 24, 2015 5:52:02 PM
 */
public class ServerTest {

    private static CommunicationAppService service = new CommunicationAppServiceImpl();

    public static void initial() {
        CustomCommunicationEndpoint endpoint_2088 = new CustomCommunicationEndpoint(2088);
        endpoint_2088.initial();

        CustomCommunicationEndpoint endpoint_2089 = new CustomCommunicationEndpoint(2089);
        endpoint_2089.initial();
    }

    public static void main(String[] args) throws Exception {
        initial();
        CommunicationRegistry.regist(AppEventType.create, service);
        while (true) {
            Thread.sleep(1000);
        }
    }
}
