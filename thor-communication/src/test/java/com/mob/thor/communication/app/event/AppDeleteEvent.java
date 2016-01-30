/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.app.event;

import com.mob.thor.communication.core.model.Event;

/**
 * @author zxc Dec 24, 2015 5:55:11 PM
 */
public class AppDeleteEvent extends Event {

    private static final long serialVersionUID = -5660518417326106999L;

    public AppDeleteEvent() {
        super(AppEventType.create);
    }

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
