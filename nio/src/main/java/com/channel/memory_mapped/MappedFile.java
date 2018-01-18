package com.channel.memory_mapped;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class MappedFile {

    public static void main(String[] args) throws Exception {
        File tempFile = File.createTempFile("mmaptest", null);
        RandomAccessFile file = new RandomAccessFile(tempFile, "rw");
        FileChannel channel = file.getChannel();

    }
}
