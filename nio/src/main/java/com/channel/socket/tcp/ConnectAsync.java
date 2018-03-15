package com.channel.socket.tcp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

// 和SocketDemo的connect2()方式一样的
public class ConnectAsync {

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 1234;
        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }
        InetSocketAddress addr = new InetSocketAddress(host, port);
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        System.out.println("initiating connection");
        sc.connect(addr);
        while (!sc.finishConnect()) {
            System.out.println("connect...");
        }
        doSomethingUseful(sc);
        System.out.println("connection established");
        sc.close();
    }

    // 从SocketChannel中获取数据并打印
    public static void doSomethingUseful(SocketChannel sc) throws Exception {
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
