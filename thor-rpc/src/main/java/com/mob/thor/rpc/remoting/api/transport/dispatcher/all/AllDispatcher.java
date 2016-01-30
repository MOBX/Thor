package com.mob.thor.rpc.remoting.api.transport.dispatcher.all;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.Dispatcher;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

/**
 * 默认的线程池配置
 * 
 * @author zxc
 */
public class AllDispatcher implements Dispatcher {

    public static final String NAME = "all";

    public ThorChannelHandler dispatch(ThorChannelHandler handler, URL url) {
        return new AllChannelHandler(handler, url);
    }

}
