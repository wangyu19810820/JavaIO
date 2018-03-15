package com.channel.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;

// 管道Pipe，是2个线程之间的单向数据连接。
// Pipe有一个source通道和一个sink通道。数据会被写到sink通道，从source通道读取。
public class PipeTest {

    public static void main(String[] args) throws IOException {
        WritableByteChannel out = Channels.newChannel(System.out);

        // 管道，sinkChannel用于写数据，sourceChannel用于读数据
        Pipe pipe = Pipe.open();
        WritableByteChannel sinkChannel = pipe.sink();
        ReadableByteChannel sourceChannel = pipe.source();

        startWorker(sinkChannel, 10);
        ByteBuffer buffer = ByteBuffer.allocate(100);
        while (sourceChannel.read(buffer) >= 0) {
            buffer.flip();
            out.write(buffer);
            buffer.clear();
        }
    }

    private static void startWorker(WritableByteChannel channel, int reps) throws IOException {
        Worker worker = new Worker(channel, reps);
        worker.start();
    }
}
