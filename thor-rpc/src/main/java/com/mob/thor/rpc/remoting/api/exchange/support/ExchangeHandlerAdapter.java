package com.mob.thor.rpc.remoting.api.exchange.support;

import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeChannel;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeHandler;
import com.mob.thor.rpc.remoting.api.telnet.support.TelnetHandlerAdapter;

public abstract class ExchangeHandlerAdapter extends TelnetHandlerAdapter implements ExchangeHandler {

    public Object reply(ExchangeChannel channel, Object msg) throws RemotingException {
        return null;
    }

}
