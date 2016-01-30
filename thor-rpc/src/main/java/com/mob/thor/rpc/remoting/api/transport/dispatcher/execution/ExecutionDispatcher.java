package com.mob.thor.rpc.remoting.api.transport.dispatcher.execution;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.Dispatcher;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

/**
 * 除发送全部使用线程池处理
 * 
 * @author zxc
 */
public class ExecutionDispatcher implements Dispatcher {

    public static final String NAME = "execution";

    public ThorChannelHandler dispatch(ThorChannelHandler handler, URL url) {
        return new ExecutionChannelHandler(handler, url);
    }

}
