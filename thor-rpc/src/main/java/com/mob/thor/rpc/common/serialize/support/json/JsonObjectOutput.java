package com.mob.thor.rpc.common.serialize.support.json;

import java.io.*;

import com.mob.thor.rpc.common.json.JSON;
import com.mob.thor.rpc.common.serialize.ObjectOutput;

public class JsonObjectOutput implements ObjectOutput {

    private final PrintWriter writer;

    private final boolean     writeClass;

    public JsonObjectOutput(OutputStream out) {
        this(new OutputStreamWriter(out), false);
    }

    public JsonObjectOutput(Writer writer) {
        this(writer, false);
    }

    public JsonObjectOutput(OutputStream out, boolean writeClass) {
        this(new OutputStreamWriter(out), writeClass);
    }

    public JsonObjectOutput(Writer writer, boolean writeClass) {
        this.writer = new PrintWriter(writer);
        this.writeClass = writeClass;
    }

    public void writeBool(boolean v) throws IOException {
        writeObject(v);
    }

    public void writeByte(byte v) throws IOException {
        writeObject(v);
    }

    public void writeShort(short v) throws IOException {
        writeObject(v);
    }

    public void writeInt(int v) throws IOException {
        writeObject(v);
    }

    public void writeLong(long v) throws IOException {
        writeObject(v);
    }

    public void writeFloat(float v) throws IOException {
        writeObject(v);
    }

    public void writeDouble(double v) throws IOException {
        writeObject(v);
    }

    public void writeUTF(String v) throws IOException {
        writeObject(v);
    }

    public void writeBytes(byte[] b) throws IOException {
        writer.println(new String(b));
    }

    public void writeBytes(byte[] b, int off, int len) throws IOException {
        writer.println(new String(b, off, len));
    }

    public void writeObject(Object obj) throws IOException {
        JSON.json(obj, writer, writeClass);
        writer.println();
        writer.flush();
    }

    public void flushBuffer() throws IOException {
        writer.flush();
    }

}
