package com.channel.socket.tcp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

// 服务端Socket通道的Demo
// ServerSocketChannel：
// 1.ServerSocketChannel.open()获取一个ServerSocketChannel
// 2.serverSocketChannel中取出ServerSocket并bind绑定一个地址
// 3.serverSocketChannel.configureBlocking(...)配置运行在阻塞模式，还是非阻塞模式
// 4.serverSocketChannel.accept()获取一个SocketChannel
// SocketChannel：write(buffer)发送缓冲区内的一段数据
public class ChannelAccept {

    public static final String GREETING = "Hello I must be going.\r\n";

    public static void main(String[] args) throws Exception {
        int port = 1234;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        ByteBuffer buffer = ByteBuffer.wrap(GREETING.getBytes());
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        while (true) {
            System.out.println("Waiting for connections");
            SocketChannel sc = ssc.accept();
            if (sc == null) {
                System.out.println("sleep 2000 millis");
                Thread.sleep(2000);
            } else {
                System.out.println("Incoming connection from: " + sc.socket().getRemoteSocketAddress());
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }
    }
}
