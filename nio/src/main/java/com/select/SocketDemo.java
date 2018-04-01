package com.select;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

// 模拟客户端Socket，配合SelectSockets，SelectSocketsThreadPool
public class SocketDemo {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 1234);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        byte[] bytes = new byte[100];
        Thread t1 = new Thread(() -> {
            try {
                while (inputStream.read(bytes) > 0) {
                    System.out.println(new String(bytes));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    outputStream.write(scanner.nextLine().getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t2.start();

        Thread.sleep(100000);
    }
}
