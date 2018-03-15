package com.channel.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import static java.lang.Thread.sleep;

// 客户端Socket的demo
// 1.SocketChannel.open()获取SocketChannel
// 2.configureBlocking(...)配置是否阻塞模式
// 3.connect(addr)非阻塞模式，虽然返回false，但是内部仍然在执行连接操作
// 4.finishConnect()测试是否连接成功
// 5.read(byteBuffer)接收数据到一个缓冲区内
public class SocketDemo {

    public static void main(String[] args) throws Exception {
        connect2();
    }

    // 通过传统方式连接
    public static void connect1() throws IOException {
        Socket socket = new Socket("localhost", 1234);
        byte[] bytes = new byte[100];
        InputStream inputStream = socket.getInputStream();
        inputStream.read(bytes);
        System.out.println(new String(bytes));
    }

    // SocketChannel方式的demo
    public static void connect2() throws Exception {
        InetSocketAddress addr = new InetSocketAddress("localhost", 1234);
        SocketChannel sc = SocketChannel.open();
//        Socket sk = sc.socket();
        sc.configureBlocking(false);
        boolean connectSuc = sc.connect(addr);  // 非阻塞模式，虽然返回false，但是内部仍然在执行连接操作
//        System.out.println(connectSuc);
        while(!sc.finishConnect()) {
            System.out.println("connect...");
        }
        // 连接成功
        System.out.println("connect finnish");
        doSomeUseful(sc);
        sc.close();
    }

    // 从SocketChannel中获取数据并打印
    public static void doSomeUseful(SocketChannel sc) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        int count = 0;
        while ((count = sc.read(byteBuffer)) != -1) {
            if (count == 0) {
                Thread.sleep(100);
            }
        }
//        System.out.println(Arrays.toString(byteBuffer.array()));
        byteBuffer.flip();
//        while (byteBuffer.hasRemaining()) {
//            System.out.print((char)byteBuffer.get());
//        }
//        System.out.println();

        System.out.println(new String(byteBuffer.array(), 0, byteBuffer.limit(), "UTF-8"));
    }
}
