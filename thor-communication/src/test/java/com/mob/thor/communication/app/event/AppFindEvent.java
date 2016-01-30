/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.app.event;

import com.mob.thor.communication.core.model.Event;

/**
 * @author zxc Dec 24, 2015 5:53:52 PM
 */
public class AppFindEvent extends Event {

    private static final long serialVersionUID = 5419014855561128447L;

    public AppFindEvent() {
        super(AppEventType.find);
    }

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
