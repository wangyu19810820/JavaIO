package com.channel.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import static java.lang.Thread.sleep;

public class SocketDemo {

    public static void main(String[] args) throws Exception {
        connect2();
    }

    public static void connect1() throws IOException {
        Socket socket = new Socket("localhost", 1234);
        byte[] bytes = new byte[100];
        InputStream inputStream = socket.getInputStream();
        inputStream.read(bytes);
        System.out.println(new String(bytes));
    }

    public static void connect2() throws Exception {
        InetSocketAddress addr = new InetSocketAddress("localhost", 1234);
        SocketChannel sc = SocketChannel.open();
//        Socket sk = sc.socket();
        sc.configureBlocking(false);
        sc.connect(addr);
        while(!sc.finishConnect()) {
            System.out.println("connect...");
        }
        System.out.println("connect finnish");
        sc.close();
    }
}
