package com.mob.thor.rpc.api.service;

public interface EchoService {

    /**
     * echo test.
     * 
     * @param message message.
     * @return message.
     */
    Object $echo(Object message);
}
