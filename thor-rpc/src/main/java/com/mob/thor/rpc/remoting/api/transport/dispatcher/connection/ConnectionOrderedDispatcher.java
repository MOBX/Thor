package com.mob.thor.rpc.remoting.api.transport.dispatcher.connection;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.Dispatcher;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

/**
 * connect disconnect 保证顺序
 * 
 * @author zxc Jan 4, 2016 11:32:34 AM
 */
public class ConnectionOrderedDispatcher implements Dispatcher {

    public static final String NAME = "connection";

    public ThorChannelHandler dispatch(ThorChannelHandler handler, URL url) {
        return new ConnectionOrderedChannelHandler(handler, url);
    }
}
