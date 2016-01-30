package com.mob.thor.rpc.common.serialize.support.nativejava;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.serialize.ObjectInput;
import com.mob.thor.rpc.common.serialize.ObjectOutput;
import com.mob.thor.rpc.common.serialize.Serialization;

public class NativeJavaSerialization implements Serialization {

    public static final String NAME = "nativejava";

    public byte getContentTypeId() {
        return 7;
    }

    public String getContentType() {
        return "x-application/nativejava";
    }

    public ObjectOutput serialize(URL url, OutputStream output) throws IOException {
        return new NativeJavaObjectOutput(output);
    }

    public ObjectInput deserialize(URL url, InputStream input) throws IOException {
        return new NativeJavaObjectInput(input);
    }
}
