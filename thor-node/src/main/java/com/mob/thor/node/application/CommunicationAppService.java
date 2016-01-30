/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.node.application;

import com.mob.thor.communication.core.model.Event;
import com.mob.thor.node.application.event.DataEvent;

/**
 * @author zxc Dec 24, 2015 5:55:32 PM
 */
public interface CommunicationAppService {

    public boolean onSend(DataEvent event);

    public boolean onPush(DataEvent event);

    public Object onPull(DataEvent event);

    public boolean onSubscribe(DataEvent event);

    public Event handleEvent(Event event);
}
