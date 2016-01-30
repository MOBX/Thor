package com.mob.thor.rpc.remoting.api.exchange.support.header;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.RemotingException;
import com.mob.thor.rpc.remoting.api.Transporters;
import com.mob.thor.rpc.remoting.api.exchange.*;
import com.mob.thor.rpc.remoting.api.transport.DecodeHandler;

public class HeaderExchanger implements Exchanger {

    public static final String NAME = "header";

    public ExchangeClient connect(URL url, ExchangeHandler handler) throws RemotingException {
        return new HeaderExchangeClient(
                                        Transporters.connect(url, new DecodeHandler(new HeaderExchangeHandler(handler))));
    }

    public ExchangeServer bind(URL url, ExchangeHandler handler) throws RemotingException {
        return new HeaderExchangeServer(Transporters.bind(url, new DecodeHandler(new HeaderExchangeHandler(handler))));
    }

}
