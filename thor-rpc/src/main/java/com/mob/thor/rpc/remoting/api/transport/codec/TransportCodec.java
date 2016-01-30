package com.mob.thor.rpc.remoting.api.transport.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mob.thor.rpc.common.serialize.ObjectInput;
import com.mob.thor.rpc.common.serialize.ObjectOutput;
import com.mob.thor.rpc.common.utils.StringUtils;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.buffer.ChannelBuffer;
import com.mob.thor.rpc.remoting.api.buffer.ChannelBufferInputStream;
import com.mob.thor.rpc.remoting.api.buffer.ChannelBufferOutputStream;
import com.mob.thor.rpc.remoting.api.transport.AbstractCodec;

public class TransportCodec extends AbstractCodec {

    public void encode(ThorChannel channel, ChannelBuffer buffer, Object message) throws IOException {
        OutputStream output = new ChannelBufferOutputStream(buffer);
        ObjectOutput objectOutput = getSerialization(channel).serialize(channel.getUrl(), output);
        encodeData(channel, objectOutput, message);
        objectOutput.flushBuffer();
    }

    public Object decode(ThorChannel channel, ChannelBuffer buffer) throws IOException {
        InputStream input = new ChannelBufferInputStream(buffer);
        return decodeData(channel, getSerialization(channel).deserialize(channel.getUrl(), input));
    }

    protected void encodeData(ThorChannel channel, ObjectOutput output, Object message) throws IOException {
        encodeData(output, message);
    }

    protected Object decodeData(ThorChannel channel, ObjectInput input) throws IOException {
        return decodeData(input);
    }

    protected void encodeData(ObjectOutput output, Object message) throws IOException {
        output.writeObject(message);
    }

    protected Object decodeData(ObjectInput input) throws IOException {
        try {
            return input.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("ClassNotFoundException: " + StringUtils.toString(e));
        }
    }
}
