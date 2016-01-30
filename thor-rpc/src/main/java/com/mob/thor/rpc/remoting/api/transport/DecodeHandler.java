package com.mob.thor.rpc.remoting.api.transport;

import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;
import com.mob.thor.rpc.remoting.api.*;
import com.mob.thor.rpc.remoting.api.exchange.Request;
import com.mob.thor.rpc.remoting.api.exchange.Response;

public class DecodeHandler extends AbstractChannelHandlerDelegate {

    private static final Logger log = LoggerFactory.getLogger(DecodeHandler.class);

    public DecodeHandler(ThorChannelHandler handler) {
        super(handler);
    }

    public void received(ThorChannel channel, Object message) throws RemotingException {
        if (message instanceof Decodeable) {
            decode(message);
        }

        if (message instanceof Request) {
            decode(((Request) message).getData());
        }

        if (message instanceof Response) {
            decode(((Response) message).getResult());
        }

        handler.received(channel, message);
    }

    private void decode(Object message) {
        if (message != null && message instanceof Decodeable) {
            try {
                ((Decodeable) message).decode();
                if (log.isDebugEnabled()) {
                    log.debug(new StringBuilder(32).append("Decode decodeable message ").append(message.getClass().getName()).toString());
                }
            } catch (Throwable e) {
                if (log.isWarnEnabled()) {
                    log.warn(new StringBuilder(32).append("Call Decodeable.decode failed: ").append(e.getMessage()).toString(),
                             e);
                }
            } // ~ end of catch
        } // ~ end of if
    } // ~ end of method decode

}
