package com.mob.thor.rpc.common.serialize.support.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.serialize.ObjectInput;
import com.mob.thor.rpc.common.serialize.ObjectOutput;
import com.mob.thor.rpc.common.serialize.Serialization;

public class JavaSerialization implements Serialization {

    public byte getContentTypeId() {
        return 3;
    }

    public String getContentType() {
        return "x-application/java";
    }

    public ObjectOutput serialize(URL url, OutputStream out) throws IOException {
        return new JavaObjectOutput(out);
    }

    public ObjectInput deserialize(URL url, InputStream is) throws IOException {
        return new JavaObjectInput(is);
    }
}
