package com.mob.thor.rpc.remoting.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Adaptive;
import com.mob.thor.rpc.common.extension.SPI;

/**
 * Codec. (SPI, Singleton, ThreadSafe)
 * 
 * @author zxc
 */
@Deprecated
@SPI
public interface Codec {

    /**
     * Need more input poison.
     * 
     * @see #decode(ThorChannel, InputStream)
     */
    Object NEED_MORE_INPUT = new Object();

    /**
     * Encode message.
     * 
     * @param channel channel.
     * @param output output stream.
     * @param message message.
     */
    @Adaptive({ Constants.CODEC_KEY })
    void encode(ThorChannel channel, OutputStream output, Object message) throws IOException;

    /**
     * Decode message.
     * 
     * @see #NEED_MORE_INPUT
     * @param channel channel.
     * @param input input stream.
     * @return message or <code>NEED_MORE_INPUT</code> poison.
     */
    @Adaptive({ Constants.CODEC_KEY })
    Object decode(ThorChannel channel, InputStream input) throws IOException;

}
