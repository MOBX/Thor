package com.mob.thor.rpc.remoting.netty3;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.*;

public class NettyTransporter implements Transporter {

    public static final String NAME = "netty";
    
    public Server bind(URL url, ThorChannelHandler listener) throws RemotingException {
        return new NettyServer(url, listener);
    }

    public Client connect(URL url, ThorChannelHandler listener) throws RemotingException {
        return new NettyClient(url, listener);
    }
}