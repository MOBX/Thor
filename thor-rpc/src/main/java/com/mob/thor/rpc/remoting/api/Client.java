package com.mob.thor.rpc.remoting.api;

import com.mob.thor.rpc.common.Resetable;

/**
 * Remoting Client. (API/SPI, Prototype, ThreadSafe) <a
 * href="http://en.wikipedia.org/wiki/Client%E2%80%93server_model">Client/Server</a>
 * 
 * @see com.mob.thor.remoting.Transporter#connect(com.mob.thor.common.URL, ChannelHandler)
 * @author zxc
 */
public interface Client extends Endpoint, ThorChannel, Resetable {

    /**
     * reconnect.
     */
    void reconnect() throws RemotingException;
}
