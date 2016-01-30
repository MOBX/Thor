package com.mob.thor.rpc.remoting.api.telnet;

import com.mob.thor.rpc.common.extension.SPI;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.ThorChannel;

@SPI
public interface TelnetHandler {

    /**
     * telnet.
     * 
     * @param channel
     * @param message
     */
    String telnet(ThorChannel channel, String message) throws RemotingException;
}
