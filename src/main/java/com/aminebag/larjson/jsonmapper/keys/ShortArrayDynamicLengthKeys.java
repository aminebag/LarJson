package com.aminebag.larjson.jsonmapper.keys;

public class ShortArrayDynamicLengthKeys extends DynamicLengthKeys {

    private short[] keys = new short[4];
    private int size = 0;

    @Override
    public DynamicLengthKeys add(long key) {
        if(key >= Short.MIN_VALUE && key <= Short.MAX_VALUE) {
            ensureLength(size + 1);
            keys[size++] = (short) key;
            return this;
        } else if(key >= Integer.MIN_VALUE && key <= Integer.MAX_VALUE) {
            return new IntegerArrayDynamicLengthKeys(keys, size).add(key);
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
            keys = new short[calculateNewLength(keys.length, length)];
        }
    }
}
