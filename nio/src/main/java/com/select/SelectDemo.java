package com.select;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

// 与选择器相关的三个对象：选择器Selector，通道SelectableChannel，选择键SelectionKey（注册情况对象）
// 获取一个新的选择器Selector.open()，关闭选择器selector.close();

// 将一个通道注册到选择器上ssc.register(selector, SelectionKey.OP_ACCEPT, o)
// 第一个参数是选择器，第二个参数是注册方法，第三个参数是附加对象
// 一个通道可注册到多个选择器上
// SelectionKey共有读、写、连接、接受四个操作，可用位运算或(|)注册多个操作
public class SelectDemo {

    private static int port = 1234;

    public static void main(String[] args) throws Exception {
        Object o = new Object();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);

        // 将通道注册到选择器上，通道必须设置成非阻塞
        Selector selector = Selector.open();
        SelectionKey selectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT, o);
//        selector.close();         // 关闭选择器，并取消注册该选择器上的所有通道

        // 通道是否注册在任意一个选择器上
        boolean isRegistered = ssc.isRegistered();
        System.out.println(isRegistered);
        // 通道在某个选择器上的选择键（注册情况对象）
        System.out.println(ssc.keyFor(selector) == selectionKey);

        // SelectionKey选择键API
//        selectionKey.cancel();                                    // 取消注册
        System.out.println(selectionKey.isValid());                 // 是否有效
        System.out.println(selectionKey.channel() == ssc);          // 通道对象
        System.out.println(selectionKey.selector() == selector);    // 选择器对象
        System.out.println(selectionKey.interestOps());             // 注册操作
        System.out.println((selectionKey.interestOps() & SelectionKey.OP_ACCEPT) != 0); // 是否注册了ACCEPT操作
        System.out.println((selectionKey.interestOps() & SelectionKey.OP_CONNECT) != 0); // 是否注册了CONNECT操作
        System.out.println(selectionKey.readyOps());                // 就绪操作
//        selectionKey.attach(new Object());                        // 重新设置附加对象
        System.out.println(selectionKey.attachment() == o);         // 取出附加对象
    }
}
