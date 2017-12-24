package com.buffer.basic;

import java.nio.CharBuffer;

public class BufferFillDrain {

    public static void main(String[] args) {
        CharBuffer buffer = CharBuffer.allocate(100);
        while(fillBuffer(buffer)) {
            buffer.flip();
            drainBuffer(buffer);
            buffer.clear();
        }
    }

    private static boolean fillBuffer(CharBuffer buffer) {
        if (index >= strings.length) {
            return false;
        }
        String string = strings[index++];
        for (int i = 0; i < string.length(); i++) {
            buffer.put(string.charAt(i));
        }
        return true;
    }

    private static int index = 0;
    private static String[] strings = {
        "A random string value",
        "The product of a infinite number of monkeys",
        "Hey hey we are the Monkees",
        "Opening act for the Monkees:Jimi Hendrix",
        "Scuse me while I kiss the fly",
        "Help me! Help me!",
    };

    public static void drainBuffer(CharBuffer buffer) {
        while (buffer.hasRemaining( )) {
            System.out.print (buffer.get( ));
        }
        System.out.println ("");
    }
}
