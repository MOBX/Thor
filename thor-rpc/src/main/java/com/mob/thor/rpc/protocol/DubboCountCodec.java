package com.mob.thor.rpc.protocol;

import java.io.IOException;

import com.mob.thor.rpc.api.RpcInvocation;
import com.mob.thor.rpc.api.RpcResult;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.remoting.api.Codec2;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.buffer.ChannelBuffer;
import com.mob.thor.rpc.remoting.api.exchange.Request;
import com.mob.thor.rpc.remoting.api.exchange.Response;
import com.mob.thor.rpc.remoting.api.exchange.support.MultiMessage;

public final class DubboCountCodec implements Codec2 {

    private DubboCodec codec = new DubboCodec();

    public void encode(ThorChannel channel, ChannelBuffer buffer, Object msg) throws IOException {
        codec.encode(channel, buffer, msg);
    }

    public Object decode(ThorChannel channel, ChannelBuffer buffer) throws IOException {
        int save = buffer.readerIndex();
        MultiMessage result = MultiMessage.create();
        do {
            Object obj = codec.decode(channel, buffer);
            if (Codec2.DecodeResult.NEED_MORE_INPUT == obj) {
                buffer.readerIndex(save);
                break;
            } else {
                result.addMessage(obj);
                logMessageLength(obj, buffer.readerIndex() - save);
                save = buffer.readerIndex();
            }
        } while (true);
        if (result.isEmpty()) {
            return Codec2.DecodeResult.NEED_MORE_INPUT;
        }
        if (result.size() == 1) {
            return result.get(0);
        }
        return result;
    }

    private void logMessageLength(Object result, int bytes) {
        if (bytes <= 0) {
            return;
        }
        if (result instanceof Request) {
            try {
                ((RpcInvocation) ((Request) result).getData()).setAttachment(Constants.INPUT_KEY, String.valueOf(bytes));
            } catch (Throwable e) {
                /* ignore */
            }
        } else if (result instanceof Response) {
            try {
                ((RpcResult) ((Response) result).getResult()).setAttachment(Constants.OUTPUT_KEY, String.valueOf(bytes));
            } catch (Throwable e) {
                /* ignore */
            }
        }
    }

}
