/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core;

import com.mob.thor.communication.core.model.Event;

/**
 * 通讯服务端点，需要在每个node上部署后，就可以通过Communication工具进行数据通讯
 * 
 * @author zxc Dec 24, 2015 4:43:41 PM
 */
public interface CommunicationEndpoint {

    /**
     * 初始化endpint
     */
    public void initial();

    /**
     * 销毁endpoint
     */
    public void destory();

    /**
     * 接受一个消息
     * 
     * @return
     */
    public Object acceptEvent(Event event);
}
