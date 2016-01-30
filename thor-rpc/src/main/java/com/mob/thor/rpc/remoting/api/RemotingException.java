package com.mob.thor.rpc.remoting.api;

import java.net.InetSocketAddress;

/**
 * RemotingException. (API, Prototype, ThreadSafe)
 * 
 * @see com.mob.thor.remoting.exchange.ResponseFuture#get()
 * @see com.mob.thor.remoting.exchange.ResponseFuture#get(int)
 * @see com.mob.thor.ThorChannel.remoting.Channel#send(Object, boolean)
 * @see com.mob.thor.remoting.exchange.ExchangeChannel#request(Object)
 * @see com.mob.thor.remoting.exchange.ExchangeChannel#request(Object, int)
 * @see com.mob.thor.remoting.Transporter#bind(com.mob.thor.common.URL, ChannelHandler)
 * @see com.mob.thor.remoting.Transporter#connect(com.mob.thor.common.URL, ChannelHandler)
 * @author zxc
 * @export
 */
public class RemotingException extends Exception {

    private static final long serialVersionUID = -3160452149606778709L;

    private InetSocketAddress localAddress;

    private InetSocketAddress remoteAddress;

    public RemotingException(ThorChannel channel, String msg) {
        this(channel == null ? null : channel.getLocalAddress(), channel == null ? null : channel.getRemoteAddress(),
             msg);
    }

    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        super(message);

        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public RemotingException(ThorChannel channel, Throwable cause) {
        this(channel == null ? null : channel.getLocalAddress(), channel == null ? null : channel.getRemoteAddress(),
             cause);
    }

    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, Throwable cause) {
        super(cause);

        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public RemotingException(ThorChannel channel, String message, Throwable cause) {
        this(channel == null ? null : channel.getLocalAddress(), channel == null ? null : channel.getRemoteAddress(),
             message, cause);
    }

    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message,
                             Throwable cause) {
        super(message, cause);

        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}
