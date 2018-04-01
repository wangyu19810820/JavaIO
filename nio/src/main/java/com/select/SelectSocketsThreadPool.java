package com.select;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

// 在多并发的场景的惯用编程模型
// 单一线程处理选择就绪通道，由线程池中的空闲线程，完成就绪通道的实际操作
// 实际功能和SelectSockets一致，模拟服务器端Socket，接入Socket，获取数据，并重新写回
public class SelectSocketsThreadPool extends SelectSockets {

    private static final int MAX_THREADS = 5;
    private ThreadPool threadPool = new ThreadPool(MAX_THREADS);

    // 由线程池中的空闲线程，完成就绪通道的实际操作
    @Override
    protected void readDataFromSocket(SelectionKey key) throws Exception {
        WorkerThread workerThread = threadPool.getWorker();
        if (workerThread == null) {
            return;
        }
        workerThread.serviceChannel(key);
    }

    public static void main(String[] args) throws Exception {
        new SelectSocketsThreadPool().go(args);
    }
}

class ThreadPool {
    List idle = new LinkedList();
    ThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            WorkerThread thread = new WorkerThread(this);
            thread.setName("Worker" + (i + 1));
            thread.start();
            idle.add(thread);
        }
    }

    WorkerThread getWorker() {
        WorkerThread workerThread = null;
        synchronized (idle) {
            if (idle.size() > 0) {
                workerThread = (WorkerThread)idle.remove(0);
            }
        }
        return workerThread;
    }

    void returnWorker(WorkerThread workerThread) {
        synchronized (idle) {
            idle.add(workerThread);
        }
    }
}

class WorkerThread extends Thread {
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private ThreadPool pool;
    private SelectionKey key;

    WorkerThread(ThreadPool pool) {
        this.pool = pool;
    }

    public synchronized void run() {
        System.out.println(this.getName() + " is ready");
        while (true) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.interrupt();
            }
            if (key == null) {
                continue;
            }
            System.out.println(this.getName() + " has been awakened");
            try {
                drainChannel(key);
            } catch (Exception e) {
                System.out.println("Caught '" + e + "' closing channel");
                // close channel and nudge selector
                try {
                    key.channel().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                key.selector().wakeup();
            }
            key = null;
            this.pool.returnWorker(this);
        }
    }

    synchronized void serviceChannel(SelectionKey key) {
        this.key = key;
        // 取消SelectionKey的读操作的就绪状态
        key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
        this.notify();
    }

    // 执行操作相同，从远端Socket读取数据，并将这些数据重新返回给远端Socket
    void drainChannel(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel)key.channel();
        int count;
        buffer.clear();

        while ((count = channel.read(buffer)) > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            buffer.clear();
        }
        if (count < 0) {
            channel.close();
            return;
        }
        key.interestOps(key.interestOps() | SelectionKey.OP_READ);
        key.selector().wakeup();
    }

}