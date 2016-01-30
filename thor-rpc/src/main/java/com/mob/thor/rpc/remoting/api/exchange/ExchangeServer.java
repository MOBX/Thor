package com.mob.thor.rpc.remoting.api.exchange;

import java.net.InetSocketAddress;
import java.util.Collection;

import com.mob.thor.rpc.remoting.api.Server;

/**
 * ExchangeServer. (API/SPI, Prototype, ThreadSafe)
 * 
 * @author zxc
 */
public interface ExchangeServer extends Server {

    /**
     * get channels.
     * 
     * @return channels
     */
    Collection<ExchangeChannel> getExchangeChannels();

    /**
     * get channel.
     * 
     * @param remoteAddress
     * @return channel
     */
    ExchangeChannel getExchangeChannel(InetSocketAddress remoteAddress);
}
