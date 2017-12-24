package com.buffer.copy;

import java.nio.CharBuffer;
import java.util.Arrays;

public class BufferCopy {

    public static void main(String[] args) {
        slice();
    }

    // slice分割一段作为视图，容量是原视图limit-position
    public static void slice() {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.position(5);
        charBuffer.limit(7);
        CharBuffer newCharBuffer = charBuffer.slice();
        charBuffer.clear();
        charBuffer.put('a');
        newCharBuffer.put('b');
        print(charBuffer);
        print(newCharBuffer);
    }

    // asReadOnlyBuffer可以创建只读视图，给只读视图赋值会抛ReadOnlyBufferException
    public static void readOnlyDuplicate() {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.position(5);
        charBuffer.limit(7);
        CharBuffer newCharBuffer = charBuffer.asReadOnlyBuffer();
        charBuffer.clear();
        charBuffer.put('a');
//        newCharBuffer.put('b');
        print(charBuffer);
        print(newCharBuffer);
    }

    // duplicate后的buffer，可称为视图buffer，拥有原buffer相同的数据，capacity，不同的limit，position
    public static void duplicate() {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.position(5);
        charBuffer.limit(7);
        CharBuffer newCharBuffer = charBuffer.duplicate();
        charBuffer.clear();
        charBuffer.put('a');
        newCharBuffer.put('b');
        print(charBuffer);
        print(newCharBuffer);
    }

    public static void print(CharBuffer buffer) {
        System.out.println("capacity:" + buffer.capacity());
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        System.out.println("isReadOnly:" + buffer.isReadOnly());

//        System.out.println(Arrays.toString(buffer.array()));

        buffer.rewind();
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get() + ",");
        }
        System.out.println();
    }
}
