package com.channel.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketDemo {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        byte[] bytes = new byte[100];
        InputStream inputStream = socket.getInputStream();
        inputStream.read(bytes);
        System.out.println(new String(bytes));
    }
}
