package com.mob.thor.rpc.remoting.api;

import java.io.IOException;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Adaptive;
import com.mob.thor.rpc.common.extension.SPI;
import com.mob.thor.rpc.remoting.api.buffer.ChannelBuffer;

@SPI
public interface Codec2 {

    @Adaptive({ Constants.CODEC_KEY })
    void encode(ThorChannel channel, ChannelBuffer buffer, Object message) throws IOException;

    @Adaptive({ Constants.CODEC_KEY })
    Object decode(ThorChannel channel, ChannelBuffer buffer) throws IOException;

    enum DecodeResult {
        NEED_MORE_INPUT, SKIP_SOME_INPUT
    }
}
