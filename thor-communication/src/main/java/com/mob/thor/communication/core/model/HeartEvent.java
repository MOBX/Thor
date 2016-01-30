/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.model;

/**
 * 简单心跳检查事件
 * 
 * @author zxc Dec 24, 2015 4:44:32 PM
 */
public class HeartEvent extends Event {

    private static final long serialVersionUID = 2399914110721936612L;

    public HeartEvent() {
        super(HeartEventType.HEARTBEAT);
    }

    private Byte heart = 1;

    public static enum HeartEventType implements EventType {
        HEARTBEAT;
    }

    public Byte getHeart() {
        return heart;
    }

    public void setHeart(Byte heart) {
        this.heart = heart;
    }
}
