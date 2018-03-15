package com.channel.socket.udp;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

// 服务器端DatagramChannel
// 1.新建DatagramChannel：DatagramChannel.open()
// 2.绑定端口:this.channel.socket().bind(new InetSocketAddress(port))
// 3.接收数据，数据保存到缓冲区，同时返回远程连接信息：SocketAddress sa = this.channel.receive(buffer);
// 4.发送数据，包含数据和远程连接信息：this.channel.send(buffer, sa);
// 其中新建数据报通道、接收数据、发送数据，客户端和服务器端都是一样的
// 服务器端多了一步绑定端口，也就是第2步
public class TimeServer {

    private static final int DEFAULT_TIME_PORT = 37;
    private static final long DIFF_1900 = 2208988800L;
    protected DatagramChannel channel;

    public TimeServer(int port) throws Exception {
        // 新建DatagramChannel
        this.channel = DatagramChannel.open();
        // 绑定端口，作为服务器端
        this.channel.socket().bind(new InetSocketAddress(port));
        System.out.println("Listener on port " + port + " for time request");
    }

    public void listen() throws Exception {
        ByteBuffer longBuffer = ByteBuffer.allocate(8);
        longBuffer.order(ByteOrder.BIG_ENDIAN);
        longBuffer.putLong(0, 0);
        longBuffer.position(4);
        ByteBuffer buffer = longBuffer.slice();
        while (true) {
            buffer.clear();

            // 收到远程数据
            SocketAddress sa = this.channel.receive(buffer);
            if (sa == null) {
                continue;
            }
            System.out.println("Time request from " + sa);

            // 打印收到的数据
            System.out.println("receive:");
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print(buffer.get());
            }
            System.out.println();

            // 发送数据
            buffer.clear();
            longBuffer.putLong(0, System.currentTimeMillis() / 1000 + DIFF_1900);
            this.channel.send(buffer, sa);
        }
    }

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_TIME_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        TimeServer ts = new TimeServer(port);
        ts.listen();
    }
}
