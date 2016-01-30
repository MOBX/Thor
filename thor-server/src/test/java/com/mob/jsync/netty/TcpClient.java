/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.jsync.netty;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * @author zxc Dec 2, 2015 3:00:59 PM
 */
public class TcpClient {

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;

        try {
            socket = new Socket("localhost", 8080);
            out = socket.getOutputStream();
            in = socket.getInputStream();
            // 请求服务器
            String data = "我是客户端";
            byte[] outputBytes = data.getBytes("UTF-8");
            out.write(LittleEndian.toLittleEndian(outputBytes.length));
            out.write(outputBytes);
            out.flush();
            // 获取响应
            byte[] inputBytes = new byte[1024];
            int length = in.read(inputBytes);
            if (length >= 4) {
                int bodyLength = LittleEndian.getLittleEndianInt(inputBytes);
                if (length >= 4 + bodyLength) {
                    byte[] bodyBytes = Arrays.copyOfRange(inputBytes, 4, 4 + bodyLength);
                    System.out.println("Header:" + bodyLength);
                    System.out.println("Body:" + new String(bodyBytes, "UTf-8"));
                }
            }
        } finally {
            in.close();
            out.close();
            socket.close();
        }
    }
}
