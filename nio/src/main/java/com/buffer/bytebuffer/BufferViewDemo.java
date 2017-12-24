package com.buffer.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

// 可将ByteBuffer转成其他类型的Buffer，由ByteBuffer的position和limit生成新视图Buffer
public class BufferViewDemo {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(9);
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte) (i + 50));
        }
        byteBuffer.flip();
        CharBuffer charBuffer = byteBuffer.asCharBuffer();
        print(charBuffer);
        System.out.println(charBuffer.isDirect());
    }

    public static void print(CharBuffer buffer) {
        System.out.println("capacity:" + buffer.capacity());
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        if (buffer.hasArray()) {
            System.out.println(Arrays.toString(buffer.array()));
        } else {
            while (buffer.hasRemaining()) {
                System.out.print(buffer.get() + ",");
            }
            System.out.println();
        }
    }
}
