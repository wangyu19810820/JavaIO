package com.select;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

// 服务器端Socket
// 用选择器获取客户端Socket接入
// 用选择器获取读取就绪Socket，并将相同内容写回客户端
// 没有取消SelectionKey的读操作响应代码，具体写法可参考SelectSocketsThreadPool
public class SelectSockets {

    public static int PORT_NUMBER = 1234;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws Exception {
        new SelectSockets().go(args);
    }

    public void go(String[] argv) throws Exception {
        int port = PORT_NUMBER;
        if (argv.length > 0) {
            port = Integer.parseInt(argv[0]);
        }
        System.out.println("Listener on port " + port);

        // init
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int n = selector.select();
            if (n == 0) {
                continue;
            }
            Iterator it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = (SelectionKey)it.next();
                // 接入新客户端
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel)key.channel();
                    SocketChannel channel = server.accept();
                    registerChannel(selector, channel, SelectionKey.OP_READ);
                    sayHello(channel);
                }

                // 从客户端读数据，并写回客户端
                if (key.isReadable()) {
                    readDataFromSocket(key);
                }

                // 从就绪集合中移除，如果下次就绪还会被重新选中，如果下次未就绪则不在选中Set中
                it.remove();
            }
        }
    }

    protected void registerChannel(Selector selector, SelectableChannel channel, int ops)
            throws Exception {
        if (channel == null) {
            return;
        }
        channel.configureBlocking(false);
        channel.register(selector, ops);
    }

    protected void readDataFromSocket(SelectionKey key) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int count;
        buffer.clear();
        while ((count = socketChannel.read(buffer)) > 0) {
            // 再写多次的时候可能出问题，将上一次末尾的内容重复写入通道了
            ByteBuffer b1 = ByteBuffer.wrap("abcdefghijklmnopqrstuvwxyz".getBytes());
            b1.limit(4);
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(b1);
            }
            buffer.clear();
        }
    }

    private void sayHello(SocketChannel channel) throws Exception {
        buffer.clear();
        buffer.put("Hi there!\r\n".getBytes());
        buffer.flip();
        channel.write(buffer);
    }
}
