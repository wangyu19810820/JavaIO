package com.channel.basic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 通道和缓冲区的基本操作（读通道将数据放入缓冲区，写通道从缓冲区取数据）
 */
public class ChannelCopy {

    public static void main(String[] args) throws IOException {
        ReadableByteChannel source = Channels.newChannel(System.in);
        WritableByteChannel dest = Channels.newChannel(System.out);
        channelCopy2(source, dest);
        source.close();
        dest.close();
    }

    private static void channelCopy1(ReadableByteChannel src, WritableByteChannel dest)
            throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        while(src.read(buffer) != -1) {
            buffer.flip();          // 缓冲区由写转成读
            dest.write(buffer);
            buffer.compact();       // 缓冲区由读转写，未读数据移至缓冲区开头，position移至limit-position,limit设置为capacity
        }

        // 写通道取走最后一部分数据
        buffer.flip();
        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }

        // 清空缓冲区所有状态
        buffer.clear();
    }

    private static void channelCopy2(ReadableByteChannel src, WritableByteChannel dest)
            throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        while (src.read(buffer) != -1) {
            buffer.flip();  // 缓冲区由写转成读

            // 取走缓冲区内所有内容
            while (buffer.hasRemaining()) {
                dest.write(buffer);
            }

            // 清空缓冲区所有状态，由读转成写
            buffer.clear();
        }

    }
}
