package com.aminebag.larjson.blueprint;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Amine Bagdouri
 *
 * This reader is based on a buffer that is used to store bytes, that are loaded from a file, into memory
 */
public class BufferBinaryReader extends AbstractBinaryReader {

    private final byte[] array;
    private int position;

    public BufferBinaryReader(int capacity) {
        this.array = new byte[capacity];
    }

    @Override
    protected byte getBackwards() {
        return array[--position];
    }

    public int capacity() {
        return array.length;
    }

    public int load(RandomAccessFile randomAccessFile) throws IOException {
        int total = 0;
        int read;
        while(total < array.length && (read = randomAccessFile.read(array, total, array.length - total)) >= 0) {
            total += read;
        }
        return total;
    }

    @Override
    public void position(long position) {
        if(position < 0 || position > array.length) {
            throw new IllegalArgumentException("position : " + position + ", length : " + array.length);
        }
        this.position = (int) position;
    }

    @Override
    public void close() {

    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public long length() {
        return array.length;
    }
}
