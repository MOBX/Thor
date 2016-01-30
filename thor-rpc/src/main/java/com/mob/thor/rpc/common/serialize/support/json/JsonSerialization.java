package com.mob.thor.rpc.common.serialize.support.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.serialize.ObjectInput;
import com.mob.thor.rpc.common.serialize.ObjectOutput;
import com.mob.thor.rpc.common.serialize.Serialization;

public class JsonSerialization implements Serialization {

    public byte getContentTypeId() {
        return 5;
    }

    public String getContentType() {
        return "text/json";
    }

    public ObjectOutput serialize(URL url, OutputStream output) throws IOException {
        return new JsonObjectOutput(output, url.getParameter("with.class", true));
    }

    public ObjectInput deserialize(URL url, InputStream input) throws IOException {
        return new JsonObjectInput(input);
    }
}
