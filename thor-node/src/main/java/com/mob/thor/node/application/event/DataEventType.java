/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.node.application.event;

import com.mob.thor.communication.core.model.EventType;

/**
 * @author zxc Dec 25, 2015 10:53:51 AM
 */
public enum DataEventType implements EventType {
    send, push, pull, subscribe;
}
