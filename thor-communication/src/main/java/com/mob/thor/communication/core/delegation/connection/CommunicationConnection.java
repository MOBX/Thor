/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.connection;

import com.mob.thor.communication.core.exception.CommunicationException;
import com.mob.thor.communication.core.model.CommunicationParam;
import com.mob.thor.communication.core.model.Event;

/**
 * 通讯链接
 * 
 * @author zxc Dec 24, 2015 4:37:27 PM
 */
public interface CommunicationConnection {

    public Object call(Event event);

    public CommunicationParam getParams();

    public void close() throws CommunicationException;
}
