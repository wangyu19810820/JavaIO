package com.channel.file;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

// truncate会截断文件，force会将更新同步到文件
// 通道position达到或超过文件末尾，读只能读到EOF，但是仍然能写
public class FileChannelApiDemo {

    public static void main(String[] args) throws Exception {
        readEnd();
    }

    private static void readEnd() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        RandomAccessFile randomAccessFile = new RandomAccessFile("FileChannelApiDemo.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        fileChannel.position(100);
        System.out.println(fileChannel.read(byteBuffer));

        byteBuffer.clear();
        byteBuffer.putChar('z');
        byteBuffer.flip();
        System.out.println(fileChannel.write(byteBuffer));
    }


    private static void truncate() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        RandomAccessFile randomAccessFile = new RandomAccessFile("FileChannelApiDemo.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        fileChannel.position(5);
        System.out.println("position:" + fileChannel.position());
        fileChannel.truncate(1000000);
        System.out.println("position:" + fileChannel.position());
        fileChannel.truncate(2);    // truncate也有可能会影响position的值
        System.out.println("position:" + fileChannel.position());
        System.out.println();
        fileChannel.force(false);   // 只同步数据，不同步元信息。因为同步元信息很可能会触发底层io操作
        System.out.println();
    }


}
