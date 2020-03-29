package com.aminebag.larjson.utils;

/**
 * @author Amine Bagdouri
 *
 * A mutable implementation of an ordered collections of long values
 */
public class MutableLongList implements LongList {

    private long[] values = new long[16];
    private int size = 0;

    public void add(long value) {
        if(size >= values.length) {
            long[] a = new long[values.length + (values.length >> 1)];
            System.arraycopy(values, 0, a, 0, size);
            values = a;
        }
        values[size++] = value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public long get(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return values[index];
    }
}
