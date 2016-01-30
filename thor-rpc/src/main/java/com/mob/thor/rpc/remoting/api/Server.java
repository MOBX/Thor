package com.mob.thor.rpc.remoting.api;

import java.net.InetSocketAddress;
import java.util.Collection;

import com.mob.thor.rpc.common.Resetable;

/**
 * Remoting Server. (API/SPI, Prototype, ThreadSafe) <a
 * href="http://en.wikipedia.org/wiki/Client%E2%80%93server_model">Client/Server</a>
 * 
 * @see com.mob.thor.remoting.Transporter#bind(com.mob.thor.common.URL, ChannelHandler)
 * @author zxc
 */
public interface Server extends Endpoint, Resetable {

    /**
     * is bound.
     * 
     * @return bound
     */
    boolean isBound();

    /**
     * get channels.
     * 
     * @return channels
     */
    Collection<ThorChannel> getChannels();

    /**
     * get channel.
     * 
     * @param remoteAddress
     * @return channel
     */
    ThorChannel getChannel(InetSocketAddress remoteAddress);
}
