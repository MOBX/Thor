/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core;

import com.mob.thor.communication.core.model.Callback;
import com.mob.thor.communication.core.model.Event;

/**
 * 通讯服务类
 * 
 * @author zxc Dec 24, 2015 4:43:12 PM
 */
public interface CommunicationClient {

    /**
     * 启动communication客户端
     */
    public void initial();

    /**
     * 关闭communication客户端
     */
    public void destory();

    /**
     * 指定对应的地址，进行event调用. 地址格式为：127.0.0.1:1099
     * 
     * @param nid
     * @param event
     */
    public Object call(final String addr, final Event event);

    /**
     * 指定对应的地址，进行event调用，并注册对应的callback接口. 地址格式为：127.0.0.1:1099
     * 
     * <pre>
     * 注意：该方法为异步调用
     * </pre>
     * 
     * @param nid
     * @param event
     */
    @SuppressWarnings("rawtypes")
    public void call(final String addr, Event event, final Callback callback);

    /**
     * 指定对应的地址列表，进行event调用. 地址格式为：127.0.0.1:1099
     * 
     * @param nid
     * @param event
     */
    public Object call(final String[] addrs, final Event event);

    /**
     * 指定对应的地址列表，进行event调用，并注册对应的callback接口. 地址格式为：127.0.0.1:1099
     * 
     * <pre>
     * 注意：该方法为异步调用
     * </pre>
     * 
     * @param nid
     * @param event
     */
    @SuppressWarnings("rawtypes")
    public void call(final String[] serveraddrs, final Event event, final Callback callback);
}
