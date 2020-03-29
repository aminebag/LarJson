package com.aminebag.larjson.utils;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @author Amine Bagdouri
 *
 * Cast {@link ByteBuffer} to {@link Buffer} to remain compatible with JRE 9 when compiled with JDK 8
 */
public class ByteBufferUtils {

    public static void limit(ByteBuffer byteBuffer, int limit) {
        ((Buffer)byteBuffer).limit(limit);
    }

    public static void position(ByteBuffer byteBuffer, int position) {
        ((Buffer)byteBuffer).position(position);
    }

    public static void flip(ByteBuffer byteBuffer) {
        ((Buffer)byteBuffer).flip();
    }

    public static void clear(ByteBuffer byteBuffer) {
        ((Buffer)byteBuffer).clear();
    }

    public static void mark(ByteBuffer byteBuffer) {
        ((Buffer)byteBuffer).mark();
    }

    public static void reset(ByteBuffer byteBuffer) {
        ((Buffer)byteBuffer).reset();
    }

    public static void rewind(ByteBuffer byteBuffer) {
        ((Buffer)byteBuffer).rewind();
    }
}
