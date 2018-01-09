package com.channel.file;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;

public class LockTest {

    public static final int SIZEOF_INT = 4;
    public static final int INDEX_START = 0;
    public static final int INDEX_COUNT = 10;
    public static final int INDEX_SIZE = INDEX_COUNT * SIZEOF_INT;
    public static final ByteBuffer buffer = ByteBuffer.allocate(INDEX_SIZE);
    private IntBuffer indexBuffer = buffer.asIntBuffer();
    private Random random = new Random();

    public static void main(String[] args) throws Exception {
        boolean write = false;
        String fileName = "LockTest.txt";
        if (args.length > 0) {
            write = "w".equals(args[0]);
        }
        RandomAccessFile raf = new RandomAccessFile(fileName, write ? "rw" : "r");
        FileChannel fileChannel = raf.getChannel();
        LockTest lockTest = new LockTest();
        if (write) {
            lockTest.doUpdates(fileChannel);
        } else {
            lockTest.doQueries(fileChannel);
        }
    }

    private void doQueries(FileChannel fileChannel) throws Exception {
        while (true) {
            println ("trying for shared lock...");
            FileLock lock = fileChannel.lock(INDEX_START, INDEX_SIZE, true);
            int reps = random.nextInt(60) + 20;
            for (int i = 0; i < reps; i++) {
                int n = random.nextInt(INDEX_COUNT);
                int position = INDEX_START + (n * SIZEOF_INT);
                buffer.clear();
                fileChannel.read(buffer, position);
                int value = indexBuffer.get(n);
                println("Index entry " + n + "=" + value);
                Thread.sleep(100);
            }
            lock.release();
            println("<sleeping>");
            Thread.sleep(random.nextInt(3000) + 500);
        }
    }

    private void doUpdates(FileChannel fileChannel) throws Exception {
        while (true) {
            println("trying for exclusive lock...");
            FileLock lock = fileChannel.lock(INDEX_START, INDEX_SIZE, false);
            updateIndex(fileChannel);
            lock.release();
            println("<sleep>");
            Thread.sleep(random.nextInt(2000) + 500);
        }
    }

    private int idxval = 1;
    private void updateIndex(FileChannel fileChannel) throws Exception {
        indexBuffer.clear();
        for (int i = 0; i < INDEX_COUNT; i++) {
            idxval++;
            println("Updating index " + i + "=" + idxval);
            indexBuffer.put(idxval);
            Thread.sleep(500);
        }
        buffer.clear();
        fileChannel.write(buffer);
    }

    private int lastLineLen = 0;
    private void println(String msg) {
        System.out.print("\n ");
        System.out.print(msg);
        for (int i = msg.length( ); i < lastLineLen; i++) {
            System.out.print (" ");
        }
        System.out.print("\n ");
        System.out.flush( );
        lastLineLen = msg.length( );
    }
}
