package com.select;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class SelectorDemo {

    static Selector selector;

    public static void main(String[] args) throws Exception {
        selector = Selector.open();
        Set<SelectionKey> keys = selector.keys();
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        m2();
    }

    // 跨线程中断的例子
    public static void m2() throws Exception {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(System.currentTimeMillis());
                    int r1 = selector.select();
                    System.out.println(r1);
                    System.out.println(System.currentTimeMillis());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        Thread.sleep(1000);

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 安全唤醒选择操作的线程
//                  selector.wakeup();
                    // 关闭选择器的同时唤醒选择操作的线程
//                  selector.close();
                    // 线程中断操作也会唤醒选择操作的线程，但是不安全
                    t1.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();
    }

    public static void m1() throws Exception {
        System.out.println(System.currentTimeMillis());

        // 阻塞，直到有通道就绪
//        int r1 = selector.select();
//        System.out.println(r1);

        // 阻塞2000毫秒，或有通道就绪
//        int r2 = selector.select(2000);
//        System.out.println(r2);

        // 立即返回就绪通道数量
//        int r3 = selector.selectNow();
//        System.out.println(r3);

        // wakeup取消阻塞，唤醒线程
        // 不单能影响进行中的select操作，还能能影响下一次的select操作
        selector.wakeup();
        int r4 = selector.select();
        System.out.println(r4);

        System.out.println(System.currentTimeMillis());
    }
}
