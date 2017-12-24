package com.buffer.basic;

import java.nio.CharBuffer;

public class BufferEqualCompare {

    public static void main(String[] args) {
        CharBuffer buffer1 = CharBuffer.allocate(10);
        CharBuffer buffer2 = CharBuffer.allocate(20);
        buffer2.position(5);
        for (int i = 0; i < 10; i++) {
            buffer1.put((char) ('a' + i));
            buffer2.put((char) ('a' + i));
        }
        buffer1.flip();
        buffer2.flip();
        System.out.println(buffer1.equals(buffer2));

        // equals比较剩余可读的元素是否相等
        buffer2.position(5);
        System.out.println(buffer1.equals(buffer2));

        // compareTo比较剩余可读的元素是否相等
        System.out.println(buffer1.compareTo(buffer2));
        buffer1.get();
        System.out.println(buffer1.compareTo(buffer2));
        buffer2.get();
        buffer2.get();
        System.out.println(buffer1.compareTo(buffer2));
    }
}
