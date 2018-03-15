package com.channel.socket.udp;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

public class TimeServer {

    private static final int DEFAULT_TIME_PORT = 37;
    private static final long DIFF_1900 = 2208988800L;
    protected DatagramChannel channel;

    public TimeServer(int port) throws Exception {
        this.channel = DatagramChannel.open();
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
            SocketAddress sa = this.channel.receive(buffer);
            if (sa == null) {
                continue;
            }
            System.out.println("Time request from " + sa);
            System.out.println("receive:");
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print(buffer.get());
            }
            System.out.println();
            buffer.clear();
            longBuffer.putLong(0, System.currentTimeMillis() / 1000 + DIFF_1900);
            this.channel.send(longBuffer, sa);
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
