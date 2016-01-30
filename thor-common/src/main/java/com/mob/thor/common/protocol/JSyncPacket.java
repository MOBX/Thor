/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.protocol;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import com.mob.thor.common.utils.RemotingSerializable;

/**
 * @author zxc Dec 8, 2015 5:26:14 PM
 */
public class JSyncPacket {

    private transient static AtomicInteger RequestId = new AtomicInteger(0);

    /** Header part */
    private int                            version   = 1;
    private int                            compression;
    private int                            opaque    = RequestId.getAndIncrement();
    private int                            flag      = 0;
    private int                            packetType;

    /** Body part */
    private transient byte[]               body;

    public enum RemotingCommandType {
        REQUEST_COMMAND, RESPONSE_COMMAND;
    }

    public enum PacketType {
        HANDSHAKE, CLIENTAUTHENTICATION, ACK, SUBSCRIPTION, UNSUBSCRIPTION, GET, MESSAGES, CLIENTACK, SHUTDOWN,
        HEARTBEAT;
    }

    public JSyncPacket() {

    }

    public JSyncPacket(PacketType packetType, byte[] body) {
        setBody(body);
        setPacketType(packetType);
    }

    public ByteBuffer encode() {
        // 1> header length size
        int length = 4;
        // 2> header data length
        byte[] headerData = RemotingSerializable.encode(this);
        length += headerData.length;
        // 3> body data length
        if (this.body != null) {
            length += body.length;
        }
        ByteBuffer result = ByteBuffer.allocate(4 + length);
        // length
        result.putInt(length);
        // header length
        result.putInt(headerData.length);
        // header data
        result.put(headerData);
        // body data;
        if (this.body != null) {
            result.put(this.body);
        }
        result.flip();
        return result;
    }

    public ByteBuffer encodeHeader() {
        return encodeHeader(this.body != null ? this.body.length : 0);
    }

    /**
     * 只打包Header，body部分独立传输
     */
    public ByteBuffer encodeHeader(final int bodyLength) {
        // 1> header length size
        int length = 4;
        // 2> header data length
        byte[] headerData = RemotingSerializable.encode(this);
        length += headerData.length;
        // 3> body data length
        length += bodyLength;
        ByteBuffer result = ByteBuffer.allocate(4 + length - bodyLength);
        // length
        result.putInt(length);
        // header length
        result.putInt(headerData.length);
        // header data
        result.put(headerData);
        result.flip();
        return result;
    }

    public static JSyncPacket decode(final byte[] array) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        return decode(byteBuffer);
    }

    public static JSyncPacket decode(final ByteBuffer byteBuffer) {
        int length = byteBuffer.limit();
        int headerLength = byteBuffer.getInt();

        byte[] headerData = new byte[headerLength];
        byteBuffer.get(headerData);

        int bodyLength = length - 4 - headerLength;
        byte[] bodyData = null;
        if (bodyLength > 0) {
            bodyData = new byte[bodyLength];
            byteBuffer.get(bodyData);
        }
        JSyncPacket cmd = RemotingSerializable.decode(headerData, JSyncPacket.class);
        cmd.body = bodyData;
        return cmd;
    }

    @Override
    public String toString() {
        return RemotingSerializable.toJson(this, true);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getCompression() {
        return compression;
    }

    public void setCompression(int compression) {
        this.compression = compression;
    }

    public int getOpaque() {
        return opaque;
    }

    public void setOpaque(int opaque) {
        this.opaque = opaque;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public void setPacketType(PacketType packetType) {
        this.packetType = packetType.ordinal();
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
