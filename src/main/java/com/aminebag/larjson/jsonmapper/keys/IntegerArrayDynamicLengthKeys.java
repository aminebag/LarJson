package com.aminebag.larjson.jsonmapper.keys;

public class IntegerArrayDynamicLengthKeys extends DynamicLengthKeys {

    private int[] keys;
    private int size = 0;

    IntegerArrayDynamicLengthKeys(short[] shortKeys, int size) {
        if(size < shortKeys.length) {
            keys = new int[shortKeys.length];
        } else {
            keys = new int[calculateNewLength(shortKeys.length, size + 1)];
        }
        for(int i=0; i<size; i++) {
            keys[i] = shortKeys[i];
        }
        this.size = size;
    }

    @Override
    public DynamicLengthKeys add(long key) {
        if(key >= Integer.MIN_VALUE && key <= Integer.MAX_VALUE) {
            ensureLength(size + 1);
            keys[size++] = (int) key;
            return this;
        } else {
            return new LongArrayDynamicLengthKeys(keys, size).add(key);
        }
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
            keys = new int[calculateNewLength(keys.length, length)];
        }
    }
}
