package com.channel.memory_mapped_file;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

// 内存映射的最简单例子
public class MemoryMappedBasic {

    private static final String DEMOGRAPHIC = "blahblah.txt";

    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile(DEMOGRAPHIC, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        System.out.println(fileChannel.size());
//        MappedByteBuffer buf = fileChannel.map(ChannelTransfer.MapMode.READ_WRITE, 0, fileChannel.size());
        MappedByteBuffer buf = fileChannel.map(FileChannel.MapMode.PRIVATE, 0, fileChannel.size());

        System.out.println("是否全部被载入内存:" + buf.isLoaded());
        MappedByteBuffer buf1 = buf.load();
        System.out.println("load方法返回的是原对象？ " + (buf == buf1));
        System.out.println("是否全部被载入内存:" + buf.isLoaded());

        buf.putChar('3');
        buf.flip();
        buf.force();

    }
}
