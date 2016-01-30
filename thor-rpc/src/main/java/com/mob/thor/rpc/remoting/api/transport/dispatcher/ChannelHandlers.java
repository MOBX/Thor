package com.mob.thor.rpc.remoting.api.transport.dispatcher;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.ExtensionLoader;
import com.mob.thor.rpc.remoting.api.Dispatcher;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.exchange.support.header.HeartbeatHandler;
import com.mob.thor.rpc.remoting.api.transport.MultiMessageHandler;

/**
 * @author zxc Jan 4, 2016 11:34:30 AM
 */
public class ChannelHandlers {

    public static ThorChannelHandler wrap(ThorChannelHandler handler, URL url) {
        return ChannelHandlers.getInstance().wrapInternal(handler, url);
    }

    protected ChannelHandlers() {
    }

    protected ThorChannelHandler wrapInternal(ThorChannelHandler handler, URL url) {
        return new MultiMessageHandler(
                                       new HeartbeatHandler(
                                                            ExtensionLoader.getExtensionLoader(Dispatcher.class).getAdaptiveExtension().dispatch(handler,
                                                                                                                                                 url)));
    }

    private static ChannelHandlers INSTANCE = new ChannelHandlers();

    protected static ChannelHandlers getInstance() {
        return INSTANCE;
    }

    static void setTestingChannelHandlers(ChannelHandlers instance) {
        INSTANCE = instance;
    }
}
