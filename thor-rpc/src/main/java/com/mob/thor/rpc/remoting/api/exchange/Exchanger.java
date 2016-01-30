package com.mob.thor.rpc.remoting.api.exchange;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.Adaptive;
import com.mob.thor.rpc.common.extension.SPI;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.exchange.support.header.HeaderExchanger;

/**
 * Exchanger. (SPI, Singleton, ThreadSafe) <a href="http://en.wikipedia.org/wiki/Message_Exchange_Pattern">Message
 * Exchange Pattern</a> <a href="http://en.wikipedia.org/wiki/Request-response">Request-Response</a>
 * 
 * @author zxc
 */
@SPI(HeaderExchanger.NAME)
public interface Exchanger {

    /**
     * bind.
     * 
     * @param url
     * @param handler
     * @return message server
     */
    @Adaptive({ Constants.EXCHANGER_KEY })
    ExchangeServer bind(URL url, ExchangeHandler handler) throws RemotingException;

    /**
     * connect.
     * 
     * @param url
     * @param handler
     * @return message channel
     */
    @Adaptive({ Constants.EXCHANGER_KEY })
    ExchangeClient connect(URL url, ExchangeHandler handler) throws RemotingException;

}
