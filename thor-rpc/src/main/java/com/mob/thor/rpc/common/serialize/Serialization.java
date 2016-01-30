package com.mob.thor.rpc.common.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.common.extension.Adaptive;
import com.mob.thor.rpc.common.extension.SPI;

/**
 * Serialization. (SPI, Singleton, ThreadSafe)
 * 
 * @author zxc
 */
@SPI("hessian2")
public interface Serialization {

    /**
     * get content type id
     * 
     * @return content type id
     */
    byte getContentTypeId();

    /**
     * get content type
     * 
     * @return content type
     */
    String getContentType();

    /**
     * create serializer
     * 
     * @param url
     * @param output
     * @return serializer
     * @throws IOException
     */
    @Adaptive
    ObjectOutput serialize(URL url, OutputStream output) throws IOException;

    /**
     * create deserializer
     * 
     * @param url
     * @param input
     * @return deserializer
     * @throws IOException
     */
    @Adaptive
    ObjectInput deserialize(URL url, InputStream input) throws IOException;
}
