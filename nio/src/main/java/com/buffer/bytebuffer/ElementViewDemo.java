package com.buffer.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.Arrays;

/**
 * ByteBuffer也可以put,get一个非byte类型的数据
 */
public class ElementViewDemo {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(7).order(ByteOrder.BIG_ENDIAN);
        CharBuffer charBuffer = byteBuffer.asCharBuffer();
        byteBuffer.put(0, (byte)0);
        byteBuffer.put(1, (byte) 'H');
        byteBuffer.put(2, (byte)0);
        byteBuffer.put(3, (byte) 'i');
        byteBuffer.put(4, (byte)0);
        byteBuffer.put(5, (byte) '!');
        byteBuffer.put(6, (byte) 0);

        System.out.println(byteBuffer.getChar());
        byteBuffer.position(2);

        System.out.println(Arrays.toString(byteBuffer.array()));
        byteBuffer.putChar('I');
        System.out.println(Arrays.toString(byteBuffer.array()));

        byteBuffer.position(6);
        System.out.println(byteBuffer.getChar());
    }


}
