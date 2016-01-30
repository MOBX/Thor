package com.mob.thor.rpc.common.serialize.support.hessian;

import java.io.*;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.serialize.*;
import com.mob.thor.rpc.common.serialize.ObjectInput;
import com.mob.thor.rpc.common.serialize.ObjectOutput;

/**
 * @author zxc Jan 4, 2016 12:15:52 PM
 */
public class Hessian2Serialization implements Serialization {

    public static final byte ID = 2;

    public byte getContentTypeId() {
        return ID;
    }

    public String getContentType() {
        return "x-application/hessian2";
    }

    public ObjectOutput serialize(URL url, OutputStream out) throws IOException {
        return new Hessian2ObjectOutput(out);
    }

    public ObjectInput deserialize(URL url, InputStream is) throws IOException {
        return new Hessian2ObjectInput(is);
    }
}
