package com.buffer.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

// 字节顺序是指占内存多于一个字节类型的数据在内存中的存放顺序
// 比如char占两个字节，前一个是高字节，后一个是低字节
// 大端字节顺序:低位地址存高字节
// 小端字节顺序:低位地址存低字节
public class ByteOrderDemo {

    public static void main(String[] args) {
        System.out.println("本地字节顺序：" + ByteOrder.nativeOrder());
        CharBuffer charBuffer = CharBuffer.allocate(10);
        System.out.println("JVM的CharBuffer字节顺序：" + charBuffer.order());

        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5});
        System.out.println("ByteBuffer默认字节顺序：" + byteBuffer.order());
        printBuffer(byteBuffer);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        System.out.println("设定了ByteBuffer字节顺序为小端字节顺序后：" + byteBuffer.order());
        printBuffer(byteBuffer);
    }

    public static void printBuffer(ByteBuffer buffer) {
        buffer.rewind();
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
            System.out.print(' ');
        }
        System.out.println();
    }
}
