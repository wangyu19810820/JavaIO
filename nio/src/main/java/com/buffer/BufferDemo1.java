package com.buffer;

import java.nio.ByteBuffer;

public class BufferDemo1 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte)'H')
                .put((byte)'e')
                .put((byte)'l')
                .put((byte)'l')
                .put((byte)'o');
        System.out.println("capacity:" + buffer.capacity());
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        // 无法直接获取mark位置
//        System.out.println("mark:" + buffer.mark());

    }
}
