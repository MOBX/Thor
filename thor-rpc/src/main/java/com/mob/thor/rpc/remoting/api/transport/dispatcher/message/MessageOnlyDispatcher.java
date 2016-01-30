package com.mob.thor.rpc.remoting.api.transport.dispatcher.message;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.Dispatcher;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

/**
 * 只有message receive使用线程池.
 * 
 * @author zxc
 */
public class MessageOnlyDispatcher implements Dispatcher {

    public static final String NAME = "message";

    public ThorChannelHandler dispatch(ThorChannelHandler handler, URL url) {
        return new MessageOnlyChannelHandler(handler, url);
    }
}
