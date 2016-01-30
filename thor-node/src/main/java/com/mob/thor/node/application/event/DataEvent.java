/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.node.application.event;

import com.lamfire.json.JSON;
import com.mob.thor.communication.core.model.Event;
import com.mob.thor.communication.core.model.EventType;

/**
 * @author zxc Dec 25, 2015 10:53:01 AM
 */
public class DataEvent extends Event {

    private static final long serialVersionUID = -6003535174894023096L;

    private String            topic;

    private Object            object;

    public DataEvent() {
        super(DataEventType.send);
    }

    public DataEvent(String topic, Object object) {
        super(DataEventType.send);
        this.topic = topic;
        this.object = object;
    }

    public DataEvent(EventType type, String topic, Object object) {
        super(type);
        this.topic = topic;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
