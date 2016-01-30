package com.mob.thor.rpc.remoting.api;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.Adaptive;
import com.mob.thor.rpc.common.extension.SPI;
import com.mob.thor.rpc.remoting.api.transport.dispatcher.all.AllDispatcher;

@SPI(AllDispatcher.NAME)
public interface Dispatcher {

    /**
     * dispatch the message to threadpool.
     * 
     * @param handler
     * @param url
     * @return channel handler
     */
    @Adaptive({Constants.DISPATCHER_KEY, "dispather", "channel.handler"}) // 后两个参数为兼容旧配置
    ThorChannelHandler dispatch(ThorChannelHandler handler, URL url);
}