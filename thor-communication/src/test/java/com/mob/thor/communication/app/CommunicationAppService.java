/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.app;

import com.mob.thor.communication.app.event.*;
import com.mob.thor.communication.core.model.Event;

/**
 * @author zxc Dec 24, 2015 5:55:32 PM
 */
public interface CommunicationAppService {

    public boolean onCreate(AppCreateEvent event);

    public boolean onUpdate(AppUpdateEvent event);

    public boolean onDelete(AppDeleteEvent event);

    public Event handleEvent(Event event);
}
