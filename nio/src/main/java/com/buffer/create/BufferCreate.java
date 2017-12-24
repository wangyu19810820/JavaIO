package com.buffer.create;

import java.nio.CharBuffer;
import java.util.Arrays;

public class BufferCreate {

    public static void main(String[] args) {
        wrap1();
    }

    public static void wrap1() {
//        StringBuffer sb = new StringBuffer();
//        CharBuffer charBuffer = CharBuffer.wrap(sb);
        CharBuffer charBuffer = CharBuffer.wrap("HelloWorld!", 2, 4);
        System.out.println(charBuffer.hasArray());
        print(charBuffer);
    }

    // 备份数组和缓冲区，共享数据，相互影响
    public static void wrap() {
        char[] arr = new char[10];
        // 使用3参数wrap，buffer容量仍是数组大小，只是初始化position和limit而已
        CharBuffer charBuffer = CharBuffer.wrap(arr, 2, 4);
//        CharBuffer charBuffer = CharBuffer.wrap(arr);
//        CharBuffer charBuffer = CharBuffer.allocate(10);
        arr[0] = 'a';
        charBuffer.position(1);
        charBuffer.put('b');
        print(arr);
        print(charBuffer);

        // 是否有备份数组，allocate生成的buffer也有备份数组，在堆中
        System.out.println(charBuffer.hasArray());

        // 获取备份数组
        System.out.println(Arrays.toString(charBuffer.array()));

        // 数组偏移量，3参数wrrap，仍然是0
        System.out.println(charBuffer.arrayOffset());
    }

    public static void print(CharBuffer buffer) {
        System.out.println("capacity:" + buffer.capacity());
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get() + ",");
        }
        System.out.println();
    }

    public static void print(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            System.out.print(',');
        }
        System.out.println();
    }
}
