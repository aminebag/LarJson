package com.aminebag.larjson.jsonmapper.keys;

public class LongArrayDynamicLengthKeys extends DynamicLengthKeys {

    private long[] keys;
    private int size = 0;

    LongArrayDynamicLengthKeys(short[] shortKeys, int size) {
        if(size < shortKeys.length) {
            keys = new long[shortKeys.length];
        } else {
            keys = new long[calculateNewLength(shortKeys.length, size + 1)];
        }
        for(int i=0; i<size; i++) {
            keys[i] = shortKeys[i];
        }
        this.size = size;
    }

    LongArrayDynamicLengthKeys(int[] intKeys, int size) {
        if(size < intKeys.length) {
            keys = new long[intKeys.length];
        } else {
            keys = new long[calculateNewLength(intKeys.length, size + 1)];
        }
        for(int i=0; i<size; i++) {
            keys[i] = intKeys[i];
        }
        this.size = size;
    }

    @Override
    public DynamicLengthKeys add(long key) {
        ensureLength(size + 1);
        keys[size++] = key;
        return this;
    }

    @Override
    public long getKey(int index) {
        if(index >= size) {
            throw new IndexOutOfBoundsException();
        } else {
            return keys[index];
        }
    }

    @Override
    public int size() {
        return size;
    }

    private void ensureLength(int length){
        if(keys.length < length) {
            keys = new long[calculateNewLength(keys.length, length)];
        }
    }
}
