package com.channel.memory_mapped_file;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedFile {

    public static void main(String[] args) throws Exception {
        // 创建一个临时文件和该文件的通道
        File tempFile = File.createTempFile("mmaptest", null);
        RandomAccessFile file = new RandomAccessFile(tempFile, "rw");
        FileChannel channel = file.getChannel();

        // 写入一些数据
        ByteBuffer temp = ByteBuffer.allocate(100);
        temp.put("This is the file content".getBytes());
        temp.flip();
        channel.write(temp, 0);

        // 在第8192个字节处，写入另一些内容
        // 8192字节相当于8k，一般是内存文件系统的另一虚拟页了
        temp.clear();
        temp.put("This is more file content".getBytes());
        temp.flip();
        channel.write(temp, 8192);

        // 在同一文件上建立3种不同的映射
        MappedByteBuffer ro = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        MappedByteBuffer rw = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
        MappedByteBuffer cow = channel.map(FileChannel.MapMode.PRIVATE, 0, channel.size());

        // 打印3个映射初始状态的内容
        System.out.println("Begin");
        showBuffers(ro, rw, cow);

        // 修改copy on write映射缓冲
        cow.position(8);
        cow.put("COW".getBytes());
        System.out.println("Change to cow buffer");
        showBuffers(ro, rw, cow);

        // 修改read/write映射缓冲(只读映射缓冲内的数据也会自动被修改)
        rw.position(9);
        rw.put(" R/W ".getBytes());
        rw.position(8192);
        rw.put(" R/W ".getBytes());
        rw.force();
        System.out.println("Change to R/W buffer");
        showBuffers(ro, rw, cow);

        // 通过通道修改文件内容(只读映射缓冲和读写映射缓冲自动修改)
        // (私有写映射缓冲：修改过的页保持原样，未修改过的页内容改变)
        temp.clear();
        temp.put("Channel write ".getBytes());
        temp.flip();
        channel.write(temp, 0);
        temp.rewind();
        channel.write(temp, 8202);
        System.out.println("Write on channel");
        showBuffers(ro, rw, cow);

        // 再次修改copy on write映射缓冲
        cow.position(8207);
        cow.put(" cow2 ".getBytes());
        System.out.println("Second change to COW buffer");
        showBuffers(ro, rw, cow);

        // 再次修改read/write映射缓冲(只读映射缓冲内的数据也会自动被修改)
        rw.position(0);
        rw.put(" R/W2 ".getBytes());
        rw.position(8210);
        rw.put(" R/W2 ".getBytes());
        rw.force();
        System.out.println("Second change to R/W buffer");
        showBuffers(ro, rw, cow);

        // 结束，清理
        channel.close();
        file.close();
        tempFile.delete();
    }

    // 显示3个内存映射buffer内的内容
    public static void showBuffers(ByteBuffer ro, ByteBuffer rw, ByteBuffer cow) throws Exception {
        dumpBuffer("R/O", ro);
        dumpBuffer("R/W", rw);
        dumpBuffer("COW", cow);
        System.out.println();
    }

    public static void dumpBuffer(String prefix, ByteBuffer buffer) throws Exception {
        System.out.print(prefix + ":'");
        int nulls = 0;
        int limit = buffer.limit();
        for (int i = 0; i < limit; i++) {
            char c = (char)buffer.get(i);
            if (c == '\u0000') {
                nulls++;
                continue;
            }
            if (nulls != 0) {
                System.out.print("|[" + nulls + " nulls]|");
                nulls = 0;
            }
            System.out.print(c);
        }
        System.out.println("'");
    }
}
