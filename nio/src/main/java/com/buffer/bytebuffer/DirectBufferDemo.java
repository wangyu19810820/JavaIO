package com.buffer.bytebuffer;

import java.nio.ByteBuffer;

// 直接缓冲区不在jvm堆栈中，非直接缓冲区在jvm堆栈中
public class DirectBufferDemo {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10);
        System.out.println(byteBuffer.isDirect());
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(10);
        System.out.println(byteBuffer1.isDirect());
    }
}
