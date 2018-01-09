package com.buffer.bytebuffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

// 可将ByteBuffer转成其他类型的Buffer
// 书上的例子
public class BufferCharView {

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

        println(byteBuffer);
        println(charBuffer);
    }

    public static void println(Buffer buffer) {
        System.out.print("pos=" + buffer.position());
        System.out.print(",limit=" + buffer.limit());
        System.out.print(",capacity=" + buffer.capacity());
        System.out.println(":" + buffer.toString());
    }
}
