package com.channel.file;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

// 通道的position和RandomAccessFile的file pointer是一致的，重合的
public class FileChannelDemo1 {

    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("blahblah.txt", "r");
        randomAccessFile.seek(10);
        FileChannel fileChannel = randomAccessFile.getChannel();
        System.out.println("file pos:" + fileChannel.position());
        randomAccessFile.seek(20);
        System.out.println("file pos:" + fileChannel.position());
        fileChannel.position(30);
        System.out.println("randomAccessFile's pointer:" + randomAccessFile.getFilePointer());
    }
}
