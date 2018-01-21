package com.channel.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

// 写法错误
// 文件锁是由整个Java虚拟机持有的,
// 如果虚拟机已经在同一个文件上持有了另一个重叠的锁,
// 将抛出OverlappingFileLockException

// isValid锁是否有效
// isShared是共享锁还是排他锁
// overlaps锁在某个区间是否和其他锁有重叠
public class FileLockDemo1 {

    public static void main(String[] args) throws Exception {
//        RandomAccessFile randomAccessFile = new RandomAccessFile("FileChannelApiDemo.txt", "rw");
//        ChannelTransfer fileChannel = randomAccessFile.getChannel();
//        fileChannel.lock(0, Long.MAX_VALUE, false);
//        System.out.println();

        Thread t1 = new Thread(() -> {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile("FileChannelApiDemo.txt", "rw");
                FileChannel fileChannel = randomAccessFile.getChannel();
                FileLock fileLock = fileChannel.lock(0, 1L, true);
//                System.out.println("file lock is valid:" + fileLock.isValid());
//                System.out.println("file lock is share:" + fileLock.isShared());
                System.out.println("t1 finish");
                fileLock.release();
                fileChannel.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile("FileChannelApiDemo.txt", "rw");
                FileChannel fileChannel = randomAccessFile.getChannel();
                FileLock fileLock = fileChannel.lock(1L, 1L, true);
                System.out.println("t2 lock overlaps:" + fileLock.overlaps(3, 2));
                System.out.println("t2 finish");
//                fileLock.release();
                fileChannel.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
    }


}
