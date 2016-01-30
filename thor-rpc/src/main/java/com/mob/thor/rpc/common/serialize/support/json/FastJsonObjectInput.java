package com.mob.thor.rpc.common.serialize.support.json;

import java.io.*;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.mob.thor.rpc.common.serialize.ObjectInput;
import com.mob.thor.rpc.common.utils.PojoUtils;

public class FastJsonObjectInput implements ObjectInput {

    private final BufferedReader reader;

    public FastJsonObjectInput(InputStream in) {
        this(new InputStreamReader(in));
    }

    public FastJsonObjectInput(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public boolean readBool() throws IOException {
        try {
            return readObject(boolean.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public byte readByte() throws IOException {
        try {
            return readObject(byte.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public short readShort() throws IOException {
        try {
            return readObject(short.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public int readInt() throws IOException {
        try {
            return readObject(int.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public long readLong() throws IOException {
        try {
            return readObject(long.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public float readFloat() throws IOException {
        try {
            return readObject(float.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public double readDouble() throws IOException {
        try {
            return readObject(double.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public String readUTF() throws IOException {
        try {
            return readObject(String.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public byte[] readBytes() throws IOException {
        return readLine().getBytes();
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        String json = readLine();
        return JSON.parse(json);
    }

    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        String json = readLine();
        return JSON.parseObject(json, cls);
    }

    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        Object value = readObject(cls);
        return (T) PojoUtils.realize(value, cls, type);
    }

    private String readLine() throws IOException, EOFException {
        String line = reader.readLine();
        if (line == null || line.trim().length() == 0) throw new EOFException();
        return line;
    }

}
