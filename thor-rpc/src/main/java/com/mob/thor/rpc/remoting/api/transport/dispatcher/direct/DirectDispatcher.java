package com.mob.thor.rpc.remoting.api.transport.dispatcher.direct;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.Dispatcher;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

/**
 * 不派发线程池。
 * 
 * @author zxc
 */
public class DirectDispatcher implements Dispatcher {

    public static final String NAME = "direct";

    public ThorChannelHandler dispatch(ThorChannelHandler handler, URL url) {
        return handler;
    }
}
