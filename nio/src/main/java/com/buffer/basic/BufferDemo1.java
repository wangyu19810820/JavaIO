package com.buffer.basic;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BufferDemo1 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte)'H')
                .put((byte)'e')
                .put((byte)'l')
                .put((byte)'l')
                .put((byte)'o');
        buffer.put(0, (byte) 'M').put((byte)'w').put((byte)1).put((byte)2).put((byte)3).put((byte)4);
//        buffer.put(0, (byte) 'M').put((byte)'w');

        clear(buffer);
    }

    // 通常用于由写状态转成读状态，limit置为position，position置为0
    public static void flip(ByteBuffer buffer) {
        print(buffer);
        buffer.flip();
        print(buffer);
    }

    // 通常用于置为重读状态，limit不变，position置为0
    public static void rewind(ByteBuffer buffer) {
        print(buffer);
        buffer.rewind();
        print(buffer);
    }

    // 通常用于读状态置为写状态，保留未读取部分并移至开头，limit置为末尾，position置为第一个可写的位置
    public static void compact(ByteBuffer buffer) {
        buffer.position(6);
        print(buffer);
        buffer.compact();
        print(buffer);
    }

    // 标记当前position位置，reset函数将position置为mark的位置
    public static void mark(ByteBuffer buffer) {
        buffer.position(2).mark().position(4).limit(6);
        buffer.reset();
        print(buffer);
    }

    // 通常用于读状态置为写状态，不保留原内容。limit置为末尾，position置为开头，实际内容未被清空
    public static void clear(ByteBuffer buffer) {
        buffer.limit(6);
        print(buffer);
        buffer.clear();
        print(buffer);
    }

    public static void print(ByteBuffer buffer) {
        System.out.println("capacity:" + buffer.capacity());
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        System.out.println(Arrays.toString(buffer.array()));

        // 无法直接获取mark位置
//        System.out.println("mark:" + buffer.mark());
    }
}
