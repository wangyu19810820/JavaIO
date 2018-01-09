package com.channel.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

// FileChannel的lock获得锁，如果无法获得，会阻塞
// tryLock也是获取锁，如果无法获得，会返回null
// FileLock的release释放锁
public class FileLockExclusiveDemo {

    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("FileChannelApiDemo.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        FileLock lock = fileChannel.tryLock(0, Long.MAX_VALUE, false);
//        FileLock lock = fileChannel.lock(0, Long.MAX_VALUE, false);
        if (lock != null) {
            System.out.println("获得锁");
            lock.release();
            System.out.println("释放锁");
        } else {
            System.out.println("锁为null");
        }

    }
}
