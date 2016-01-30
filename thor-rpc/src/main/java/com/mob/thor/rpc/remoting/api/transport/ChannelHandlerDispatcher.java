package com.mob.thor.rpc.remoting.api.transport;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

public class ChannelHandlerDispatcher implements ThorChannelHandler {

    private static final Logger                  logger          = LoggerFactory.getLogger(ChannelHandlerDispatcher.class);

    private final Collection<ThorChannelHandler> channelHandlers = new CopyOnWriteArraySet<ThorChannelHandler>();

    public ChannelHandlerDispatcher() {
    }

    public ChannelHandlerDispatcher(ThorChannelHandler... handlers) {
        this(handlers == null ? null : Arrays.asList(handlers));
    }

    public ChannelHandlerDispatcher(Collection<ThorChannelHandler> handlers) {
        if (handlers != null && handlers.size() > 0) {
            this.channelHandlers.addAll(handlers);
        }
    }

    public Collection<ThorChannelHandler> getChannelHandlers() {
        return channelHandlers;
    }

    public ChannelHandlerDispatcher addChannelHandler(ThorChannelHandler handler) {
        this.channelHandlers.add(handler);
        return this;
    }

    public ChannelHandlerDispatcher removeChannelHandler(ThorChannelHandler handler) {
        this.channelHandlers.remove(handler);
        return this;
    }

    public void connected(ThorChannel channel) {
        for (ThorChannelHandler listener : channelHandlers) {
            try {
                listener.connected(channel);
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            }
        }
    }

    public void disconnected(ThorChannel channel) {
        for (ThorChannelHandler listener : channelHandlers) {
            try {
                listener.disconnected(channel);
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            }
        }
    }

    public void sent(ThorChannel channel, Object message) {
        for (ThorChannelHandler listener : channelHandlers) {
            try {
                listener.sent(channel, message);
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            }
        }
    }

    public void received(ThorChannel channel, Object message) {
        for (ThorChannelHandler listener : channelHandlers) {
            try {
                listener.received(channel, message);
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            }
        }
    }

    public void caught(ThorChannel channel, Throwable exception) {
        for (ThorChannelHandler listener : channelHandlers) {
            try {
                listener.caught(channel, exception);
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            }
        }
    }

}
