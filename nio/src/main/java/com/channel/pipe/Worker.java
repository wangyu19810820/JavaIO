package com.channel.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Random;

public class Worker extends Thread {

    WritableByteChannel channel;

    private int reps;

    public Worker(WritableByteChannel channel, int reps) {
        this.channel = channel;
        this.reps = reps;
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        try {
            for (int i = 0; i < reps; i++) {
                Thread.sleep(1000);
                doSomeWork(buffer);
                while (channel.write(buffer) > 0) {
                    // empty
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] products = {
            "No good deed goes unpunished",
            "To be, or what?",
            "No matter where you go, there you are",
            "Just say \"Yo\"",
            "My karma ran over my dogma"
    };

    private Random random = new Random();

    private void doSomeWork(ByteBuffer buffer) {
        int productIndex = random.nextInt(products.length);
        buffer.clear();
        buffer.put(products[productIndex].getBytes());
        buffer.put("\r\n".getBytes());
        buffer.flip();
    }
}
