/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.app;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mob.thor.communication.app.event.*;
import com.mob.thor.communication.core.model.Event;

/**
 * @author zxc Dec 24, 2015 5:55:40 PM
 */
public class CommunicationAppServiceImpl implements CommunicationAppService {

    private static final Logger logger = LoggerFactory.getLogger(CommunicationAppServiceImpl.class);

    private Map<String, Event> events = new ConcurrentHashMap<String, Event>();

    public Event handleEvent(Event event) {
        throw new IllegalArgumentException();
    }

    public boolean onCreate(AppCreateEvent event) {
        events.put(event.getType().name(), event);
        logger.error("[type:{},data:{}]", event.getType(), event);
        return true;
    }

    public boolean onDelete(AppDeleteEvent event) {
        events.remove(event.getType().name());
        logger.error("[type:{},data:{}]", event.getType(), event);
        return true;
    }

    public boolean onUpdate(AppUpdateEvent event) {
        events.put(event.getType().name(), event);
        logger.error("[type:{},data:{}]", event.getType(), event);
        return true;
    }
}
