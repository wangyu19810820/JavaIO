package com.channel.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileHole {

    public static void main(String[] args) throws IOException {
        File temp = File.createTempFile("hole", null);
//        File temp = new File("hole.txt");
        RandomAccessFile file = new RandomAccessFile(temp, "rw");
        FileChannel channel = file.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
        putData(0, byteBuffer, channel);
        putData(5000000, byteBuffer, channel);
        putData(50000, byteBuffer, channel);
        System.out.println("Wrote temp file path:" + temp.getAbsolutePath() + ",size:" + channel.size());
        channel.close();
        file.close();
    }

    private static void putData(int position, ByteBuffer byteBuffer, FileChannel channel)
            throws IOException {
        String string = "*<-- location " + position;
        byteBuffer.clear();
        byteBuffer.put(string.getBytes("US-ASCII"));
        byteBuffer.flip();
        channel.position(position);
        channel.write(byteBuffer);
    }
}
