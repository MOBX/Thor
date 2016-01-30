package com.mob.thor.rpc.remoting.api.transport;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.*;

public abstract class AbstractPeer implements Endpoint, ThorChannelHandler {

    private final ThorChannelHandler handler;

    private volatile URL             url;

    private volatile boolean         closed;

    public AbstractPeer(URL url, ThorChannelHandler handler) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.url = url;
        this.handler = handler;
    }

    public void send(Object message) throws RemotingException {
        send(message, url.getParameter(Constants.SENT_KEY, false));
    }

    public void close() {
        closed = true;
    }

    public void close(int timeout) {
        close();
    }

    public URL getUrl() {
        return url;
    }

    protected void setUrl(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        this.url = url;
    }

    public ThorChannelHandler getChannelHandler() {
        if (handler instanceof ChannelHandlerDelegate) {
            return ((ChannelHandlerDelegate) handler).getHandler();
        } else {
            return handler;
        }
    }

    /**
     * @return ChannelHandler
     */
    @Deprecated
    public ThorChannelHandler getHandler() {
        return getDelegateHandler();
    }

    /**
     * 返回最终的handler，可能已被wrap,需要区别于getChannelHandler
     * 
     * @return ChannelHandler
     */
    public ThorChannelHandler getDelegateHandler() {
        return handler;
    }

    public boolean isClosed() {
        return closed;
    }

    public void connected(ThorChannel ch) throws RemotingException {
        if (closed) {
            return;
        }
        handler.connected(ch);
    }

    public void disconnected(ThorChannel ch) throws RemotingException {
        handler.disconnected(ch);
    }

    public void sent(ThorChannel ch, Object msg) throws RemotingException {
        if (closed) {
            return;
        }
        handler.sent(ch, msg);
    }

    public void received(ThorChannel ch, Object msg) throws RemotingException {
        if (closed) {
            return;
        }
        handler.received(ch, msg);
    }

    public void caught(ThorChannel ch, Throwable ex) throws RemotingException {
        handler.caught(ch, ex);
    }
}
