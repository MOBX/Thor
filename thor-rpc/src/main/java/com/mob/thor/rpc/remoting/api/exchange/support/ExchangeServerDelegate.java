package com.mob.thor.rpc.remoting.api.exchange.support;

import java.net.InetSocketAddress;
import java.util.Collection;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.ThorChannelHandler;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeChannel;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeServer;

public class ExchangeServerDelegate implements ExchangeServer {

    private transient ExchangeServer server;

    public ExchangeServerDelegate() {
    }

    public ExchangeServerDelegate(ExchangeServer server) {
        setServer(server);
    }

    public ExchangeServer getServer() {
        return server;
    }

    public void setServer(ExchangeServer server) {
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

    public boolean isClosed() {
        return server.isClosed();
    }

    public Collection<ExchangeChannel> getExchangeChannels() {
        return server.getExchangeChannels();
    }

    public ExchangeChannel getExchangeChannel(InetSocketAddress remoteAddress) {
        return server.getExchangeChannel(remoteAddress);
    }

    public void close(int timeout) {
        server.close(timeout);
    }

}
