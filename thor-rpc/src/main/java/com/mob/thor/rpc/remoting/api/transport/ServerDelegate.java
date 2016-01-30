package com.mob.thor.rpc.remoting.api.transport;

import java.net.InetSocketAddress;
import java.util.Collection;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.*;

public class ServerDelegate implements Server {

    private transient Server server;

    public ServerDelegate() {
    }

    public ServerDelegate(Server server) {
        setServer(server);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public boolean isBound() {
        return server.isBound();
    }

    public void reset(URL url) {
        server.reset(url);
    }

    public Collection<ThorChannel> getChannels() {
        return server.getChannels();
    }

    public ThorChannel getChannel(InetSocketAddress remoteAddress) {
        return server.getChannel(remoteAddress);
    }

    public URL getUrl() {
        return server.getUrl();
    }

    public ThorChannelHandler getChannelHandler() {
        return server.getChannelHandler();
    }

    public InetSocketAddress getLocalAddress() {
        return server.getLocalAddress();
    }

    public void send(Object message) throws RemotingException {
        server.send(message);
    }

    public void send(Object message, boolean sent) throws RemotingException {
        server.send(message, sent);
    }

    public void close() {
        server.close();
    }

    public void close(int timeout) {
        server.close(timeout);
    }

    public boolean isClosed() {
        return server.isClosed();
    }
}
