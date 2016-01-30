package com.mob.thor.rpc.remoting.api;

import javax.sound.midi.Receiver;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.Adaptive;
import com.mob.thor.rpc.common.extension.SPI;

/**
 * Transporter. (SPI, Singleton, ThreadSafe) <a href="http://en.wikipedia.org/wiki/Transport_Layer">Transport Layer</a>
 * <a href="http://en.wikipedia.org/wiki/Client%E2%80%93server_model">Client/Server</a>
 * 
 * @see com.mob.thor.remoting.Transporters
 * @author zxc
 */
@SPI("netty")
public interface Transporter {

    /**
     * Bind a server.
     * 
     * @see com.mob.thor.remoting.Transporters#bind(URL, Receiver, ThorChannelHandler)
     * @param url server url
     * @param handler
     * @return server
     * @throws RemotingException
     */
    @Adaptive({ Constants.SERVER_KEY, Constants.TRANSPORTER_KEY })
    Server bind(URL url, ThorChannelHandler handler) throws RemotingException;

    /**
     * Connect to a server.
     * 
     * @see com.mob.thor.remoting.Transporters#connect(URL, Receiver, ChannelListener)
     * @param url server url
     * @param handler
     * @return client
     * @throws RemotingException
     */
    @Adaptive({ Constants.CLIENT_KEY, Constants.TRANSPORTER_KEY })
    Client connect(URL url, ThorChannelHandler handler) throws RemotingException;

}
