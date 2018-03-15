package com.channel.socket.udp;

import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// 客户端DatagramChannel
// 1.新建DatagramChannel：DatagramChannel.open();
// 2.发送数据到指定地址：channel.send(buffer, inetSocketAddress);
// 3.接收数据，数据保存到缓冲区，同时返回远程连接信息：channel.receive(buffer)
public class TimeClient {

    private static final int DEFAULT_TIME_PORT = 37;
    private static final long DIFF_1900 = 2208988800L;
    protected int port = DEFAULT_TIME_PORT;
    protected List remoteHosts;
    protected DatagramChannel channel;

    public TimeClient(String[] args) throws Exception {
        if (args.length == 0) {
            throw new Exception("Usage: [ -p port ] host ...");
        }
        parseArgs(args);
        // 新建DatagramChannel
        this.channel = DatagramChannel.open();
    }

    // 向时间服务器发送数据
    protected void sendRequests() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        // 程序支持多个时间服务器
        Iterator it = remoteHosts.iterator();
        while (it.hasNext()) {
            // 取出一个时间服务器地址
            InetSocketAddress inetSocketAddress = (InetSocketAddress) it.next();
            System.out.println("Requesting time from " + inetSocketAddress.getHostName() + ":" + inetSocketAddress.getPort());
            buffer.put((byte)2);
            buffer.put((byte)3);
            buffer.flip();
            // 发送数据，包含缓冲区内的数据和地址信息
            channel.send(buffer, inetSocketAddress);
        }
    }

    // 从多个时间服务器，接收数据，并打印时间信息
    protected void getReplies() throws Exception {
        ByteBuffer longBuffer = ByteBuffer.allocate(8);
        longBuffer.order(ByteOrder.BIG_ENDIAN);
        longBuffer.putLong(0, 0);
        longBuffer.position(4);
        ByteBuffer buffer = longBuffer.slice();
        int expect = remoteHosts.size();
        int replies = 0;
        System.out.println();
        System.out.println("Waiting for replies...");
        while (true) {
            InetSocketAddress sa;
            sa = receivePacket(channel, buffer);
            buffer.flip();
            replies++;
            printTime(longBuffer.getLong(0), sa);

            if (replies == expect) {
                System.out.println("All packets answered");
                break;
            }
            // some replies haven't shown up yet
            System.out.println("Received " + replies + " of " + expect + " replies");
        }
    }

    // 单纯的接收远端数据到缓冲区
    protected InetSocketAddress receivePacket(DatagramChannel channel, ByteBuffer buffer)
            throws Exception {
        buffer.clear();
        return (InetSocketAddress)channel.receive(buffer);
    }

    // 打印时间
    protected void printTime(long remote1900, InetSocketAddress sa) {
        // local time as seconds since Jan 1, 1900
        long local = System.currentTimeMillis() / 1000;
        // remote time as seconds since Jan 1, 1900
        long remote = remote1900 - DIFF_1900;
        Date remoteDate = new Date(remote * 1000);
        Date localDate = new Date(local * 1000);
        long skew = remote - local;
        System.out.println("Reply from " + sa.getHostName() + ":" + sa.getPort());
        System.out.println("there:" + remoteDate);
        System.out.println("here:" + localDate);
        if (skew == 0) {
            System.out.println("none");
        } else if (skew > 0) {
            System.out.println(skew + " seconds ahead");
        } else {
            System.out.println((-skew) + " seconds behand");
        }
    }

    // 解析参数，使程序可以从多个时间服务器上获取时间
    protected void parseArgs(String[] args) {
        remoteHosts = new LinkedList();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-p")) {
                i++;
                this.port = Integer.parseInt(args[i]);
                continue;
            }
            InetSocketAddress sa = new InetSocketAddress(arg, port);
            if (sa.getAddress() == null) {
                System.out.println("Cannot resolve address:" + arg);
                continue;
            }
            remoteHosts.add(sa);
        }
    }

    public static void main(String[] args) throws Exception {
        TimeClient client = new TimeClient(new String[]{"-p", "37", "127.0.0.1"});
        client.sendRequests();
        client.getReplies();
    }
}
