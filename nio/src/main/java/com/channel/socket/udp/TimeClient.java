package com.channel.socket.udp;

import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
        this.channel = DatagramChannel.open();
    }

    protected void sendRequests() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        Iterator it = remoteHosts.iterator();
        while (it.hasNext()) {
            InetSocketAddress sa = (InetSocketAddress) it.next();
            System.out.println("Requesting time from " + sa.getHostName() + ":" + sa.getPort());
            buffer.put((byte)2);
            buffer.put((byte)3);
            buffer.flip();
            channel.send(buffer, sa);
        }
    }

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
        }
    }

    protected InetSocketAddress receivePacket(DatagramChannel channel, ByteBuffer buffer)
            throws Exception {
        buffer.clear();
        return (InetSocketAddress)channel.receive(buffer);
    }

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
