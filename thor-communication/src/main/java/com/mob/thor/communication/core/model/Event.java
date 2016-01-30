/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.mob.thor.common.utils.ThorToStringStyle;

/**
 * 通讯事件对象
 * 
 * @author zxc Dec 24, 2015 4:48:31 PM
 */
public abstract class Event implements Serializable {

    private static final long serialVersionUID = 208038167977229245L;

    private EventType         type;

    protected Event(){
    }

    protected Event(EventType type){
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ThorToStringStyle.DEFAULT_STYLE);
    }
}
