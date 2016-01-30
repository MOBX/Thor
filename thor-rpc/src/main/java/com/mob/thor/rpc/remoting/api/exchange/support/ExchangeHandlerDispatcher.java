package com.mob.thor.rpc.remoting.api.exchange.support;

import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeChannel;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeHandler;
import com.mob.thor.rpc.remoting.api.telnet.TelnetHandler;
import com.mob.thor.rpc.remoting.api.telnet.support.TelnetHandlerAdapter;
import com.mob.thor.rpc.remoting.api.transport.ChannelHandlerDispatcher;

public class ExchangeHandlerDispatcher implements ExchangeHandler {

    private final ReplierDispatcher        replierDispatcher;

    private final ChannelHandlerDispatcher handlerDispatcher;

    private final TelnetHandler            telnetHandler;

    public ExchangeHandlerDispatcher() {
        replierDispatcher = new ReplierDispatcher();
        handlerDispatcher = new ChannelHandlerDispatcher();
        telnetHandler = new TelnetHandlerAdapter();
    }

    public ExchangeHandlerDispatcher(Replier<?> replier) {
        replierDispatcher = new ReplierDispatcher(replier);
        handlerDispatcher = new ChannelHandlerDispatcher();
        telnetHandler = new TelnetHandlerAdapter();
    }

    public ExchangeHandlerDispatcher(ThorChannelHandler... handlers) {
        replierDispatcher = new ReplierDispatcher();
        handlerDispatcher = new ChannelHandlerDispatcher(handlers);
        telnetHandler = new TelnetHandlerAdapter();
    }

    public ExchangeHandlerDispatcher(Replier<?> replier, ThorChannelHandler... handlers) {
        replierDispatcher = new ReplierDispatcher(replier);
        handlerDispatcher = new ChannelHandlerDispatcher(handlers);
        telnetHandler = new TelnetHandlerAdapter();
    }

    public ExchangeHandlerDispatcher addChannelHandler(ThorChannelHandler handler) {
        handlerDispatcher.addChannelHandler(handler);
        return this;
    }

    public ExchangeHandlerDispatcher removeChannelHandler(ThorChannelHandler handler) {
        handlerDispatcher.removeChannelHandler(handler);
        return this;
    }

    public <T> ExchangeHandlerDispatcher addReplier(Class<T> type, Replier<T> replier) {
        replierDispatcher.addReplier(type, replier);
        return this;
    }

    public <T> ExchangeHandlerDispatcher removeReplier(Class<T> type) {
        replierDispatcher.removeReplier(type);
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object reply(ExchangeChannel channel, Object request) throws RemotingException {
        return ((Replier) replierDispatcher).reply(channel, request);
    }

    public void connected(ThorChannel channel) {
        handlerDispatcher.connected(channel);
    }

    public void disconnected(ThorChannel channel) {
        handlerDispatcher.disconnected(channel);
    }

    public void sent(ThorChannel channel, Object message) {
        handlerDispatcher.sent(channel, message);
    }

    public void received(ThorChannel channel, Object message) {
        handlerDispatcher.received(channel, message);
    }

    public void caught(ThorChannel channel, Throwable exception) {
        handlerDispatcher.caught(channel, exception);
    }

    public String telnet(ThorChannel channel, String message) throws RemotingException {
        return telnetHandler.telnet(channel, message);
    }
}
