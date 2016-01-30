package com.mob.thor.rpc.remoting.api.transport.dispatcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.ExtensionLoader;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;
import com.mob.thor.rpc.common.store.DataStore;
import com.mob.thor.rpc.common.threadpool.ThreadPool;
import com.mob.thor.rpc.common.utils.NamedThreadFactory;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.transport.ChannelHandlerDelegate;

public class WrappedChannelHandler implements ChannelHandlerDelegate {

    protected static final Logger          logger          = LoggerFactory.getLogger(WrappedChannelHandler.class);

    protected static final ExecutorService SHARED_EXECUTOR = Executors.newCachedThreadPool(new NamedThreadFactory(
                                                                                                                  "ThorSharedHandler",
                                                                                                                  true));

    protected final ExecutorService        executor;

    protected final ThorChannelHandler     handler;

    protected final URL                    url;

    public WrappedChannelHandler(ThorChannelHandler handler, URL url) {
        this.handler = handler;
        this.url = url;
        executor = (ExecutorService) ExtensionLoader.getExtensionLoader(ThreadPool.class).getAdaptiveExtension().getExecutor(url);

        String componentKey = Constants.EXECUTOR_SERVICE_COMPONENT_KEY;
        if (Constants.CONSUMER_SIDE.equalsIgnoreCase(url.getParameter(Constants.SIDE_KEY))) {
            componentKey = Constants.CONSUMER_SIDE;
        }
        DataStore dataStore = ExtensionLoader.getExtensionLoader(DataStore.class).getDefaultExtension();
        dataStore.put(componentKey, Integer.toString(url.getPort()), executor);
    }

    public void close() {
        try {
            if (executor instanceof ExecutorService) {
                ((ExecutorService) executor).shutdown();
            }
        } catch (Throwable t) {
            logger.warn("fail to destroy thread pool of server: " + t.getMessage(), t);
        }
    }

    public void connected(ThorChannel channel) throws RemotingException {
        handler.connected(channel);
    }

    public void disconnected(ThorChannel channel) throws RemotingException {
        handler.disconnected(channel);
    }

    public void sent(ThorChannel channel, Object message) throws RemotingException {
        handler.sent(channel, message);
    }

    public void received(ThorChannel channel, Object message) throws RemotingException {
        handler.received(channel, message);
    }

    public void caught(ThorChannel channel, Throwable exception) throws RemotingException {
        handler.caught(channel, exception);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public ThorChannelHandler getHandler() {
        if (handler instanceof ChannelHandlerDelegate) {
            return ((ChannelHandlerDelegate) handler).getHandler();
        } else {
            return handler;
        }
    }

    public URL getUrl() {
        return url;
    }

}
