package com.buffer.basic;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

public class BatchMove {

    public static void main(String[] args) {
        testStringToBuffer();
    }

    public static void testStringToBuffer() {
        CharBuffer buffer = CharBuffer.allocate(5);
        buffer.put("abcdefg");
        print(buffer);
        buffer.clear();
        buffer.put("abcdefg", 2, 4);
        print(buffer);
    }

    // 将数据从流拷贝到流中
    // 如果目标流空间小于源流空间，则抛异常BufferOverflowException
    // 如果同一个流拷贝，则抛异常IllegalArgumentException
    public static void testBufferToBuffer() {
        ByteBuffer srcBuffer = ByteBuffer.allocate(10);
        for (int i = 0; i < srcBuffer.capacity(); i++) {
            srcBuffer.put((byte) i);
        }
        srcBuffer.flip();
        ByteBuffer destBuffer = ByteBuffer.allocate(15);
        srcBuffer.put(srcBuffer);
        print(srcBuffer);
        print(destBuffer);
    }

    public static void testPut() {
        char[] charArr = new char[10];
        for (int i = 0; i < charArr.length; i++) {
            charArr[i] = (char)('a' + i);
        }
        CharBuffer charBuffer = CharBuffer.allocate(5);
        // 将数据从数组拷贝到流中，如果数组长度过大，将抛异常BufferOverflowException
//        charBuffer.put(charArr);
        charBuffer.put(charArr, 0, Math.min(charBuffer.remaining(), charArr.length));
        print(charBuffer);
    }
    public static void testGet() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        buffer.flip();

//        byte[] array = new byte[20];
        byte[] array = new byte[5];
        get1(buffer, array);
        print(buffer);
        System.out.println(Arrays.toString(array));
    }

    // 将数据从流拷贝到数组中，如果数组长度过大，将抛异常BufferUnderflowException
    public static void get(ByteBuffer buffer, byte[] array) {
        buffer.get(array);
    }

    // 将数据从流拷贝到数组中，并指定拷贝长度
    public static void get1(ByteBuffer buffer, byte[] array) {
        int count = Math.min(buffer.remaining(), array.length);
        buffer.get(array, 0, count);
    }

    public static void print(ByteBuffer buffer) {
        System.out.println("capacity:" + buffer.capacity());
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        System.out.println(Arrays.toString(buffer.array()));
    }

    public static void print(CharBuffer buffer) {
        System.out.println("capacity:" + buffer.capacity());
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        System.out.println(Arrays.toString(buffer.array()));
    }
}
