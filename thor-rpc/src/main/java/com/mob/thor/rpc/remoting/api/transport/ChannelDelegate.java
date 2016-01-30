package com.mob.thor.rpc.remoting.api.transport;

import java.net.InetSocketAddress;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;

public class ChannelDelegate implements ThorChannel {

    private transient ThorChannel channel;

    public ChannelDelegate() {
    }

    public ChannelDelegate(ThorChannel channel) {
        setChannel(channel);
    }

    public ThorChannel getChannel() {
        return channel;
    }

    public void setChannel(ThorChannel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel == null");
        }
        this.channel = channel;
    }

    public URL getUrl() {
        return channel.getUrl();
    }

    public InetSocketAddress getRemoteAddress() {
        return channel.getRemoteAddress();
    }

    public ThorChannelHandler getChannelHandler() {
        return channel.getChannelHandler();
    }

    public boolean isConnected() {
        return channel.isConnected();
    }

    public InetSocketAddress getLocalAddress() {
        return channel.getLocalAddress();
    }

    public boolean hasAttribute(String key) {
        return channel.hasAttribute(key);
    }

    public void send(Object message) throws RemotingException {
        channel.send(message);
    }

    public Object getAttribute(String key) {
        return channel.getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        channel.setAttribute(key, value);
    }

    public void send(Object message, boolean sent) throws RemotingException {
        channel.send(message, sent);
    }

    public void removeAttribute(String key) {
        channel.removeAttribute(key);
    }

    public void close() {
        channel.close();
    }

    public void close(int timeout) {
        channel.close(timeout);
    }

    public boolean isClosed() {
        return channel.isClosed();
    }

}
