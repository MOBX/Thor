/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.app.event;

import com.mob.thor.communication.core.model.EventType;

/**
 * @author zxc Dec 24, 2015 5:53:52 PM
 */
public enum AppEventType implements EventType {
    create, update, delete, find;
}
