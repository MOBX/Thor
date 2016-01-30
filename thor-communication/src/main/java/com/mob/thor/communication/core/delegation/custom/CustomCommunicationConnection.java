/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.custom;

import com.mob.thor.communication.core.CommunicationEndpoint;
import com.mob.thor.communication.core.delegation.connection.CommunicationConnection;
import com.mob.thor.communication.core.exception.CommunicationException;
import com.mob.thor.communication.core.model.CommunicationParam;
import com.mob.thor.communication.core.model.Event;

/**
 * @author zxc Dec 24, 2015 3:45:00 PM
 */
public class CustomCommunicationConnection implements CommunicationConnection {

    private CommunicationEndpoint endpoint;
    private CommunicationParam    params;

    public CustomCommunicationConnection(CommunicationParam params, CommunicationEndpoint endpoint){
        this.params = params;
        this.endpoint = endpoint;
    }

    public void close() throws CommunicationException {
        // do nothing
    }

    public Object call(Event event) {
        // 调用rmi传递数据到目标server上
        return endpoint.acceptEvent(event);
    }

    @Override
    public CommunicationParam getParams() {
        return params;
    }
}
