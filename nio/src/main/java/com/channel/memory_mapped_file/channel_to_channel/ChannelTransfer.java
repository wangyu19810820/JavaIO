package com.channel.memory_mapped_file.channel_to_channel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * FileChannel独有的transferTo，transferFrom可以流对流的传输数据
 * 第一个参数是位置，第二个参数是传输最大字节数，第三个参数是目标流
 * 返回值是实际传输字节数，可能小于最大字节数
 */
public class ChannelTransfer {

    public static void main(String[] args) throws Exception {
        String[] fileNames = new String[]{"blahblah.txt", "FileChannelApiDemo.txt"};
        catFiles(fileNames, Channels.newChannel(System.out));
    }

    public static void catFiles(String[] fileNames, WritableByteChannel target) throws Exception {
        for (int i = 0; i < fileNames.length; i++) {
            FileInputStream fis = new FileInputStream(new File(fileNames[i]));
            FileChannel fc = fis.getChannel();
            long actualSize = fc.transferTo(0, fc.size(), target);
            System.out.println(actualSize);
            fc.close();
            fis.close();
        }
    }
}
