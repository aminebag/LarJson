package com.aminebag.larjson.jsonmapper.keys;

import java.util.List;

abstract class LongArrayFixedLengthKeys extends FixedLengthKeys {
    final long[] keys;

    LongArrayFixedLengthKeys(int length) {
        this.keys = new long[length];
    }

    LongArrayFixedLengthKeys(long[] keys) {
        this.keys = keys;
    }

    public LongArrayFixedLengthKeys(int[] intKeys, int minIndex, int maxIndex) {
        this.keys = new long[intKeys.length];
        for(int i=minIndex; i<=maxIndex; i++) {
            this.keys[i] = intKeys[i];
        }
    }

    public LongArrayFixedLengthKeys(short[] shortKeys, int minIndex, int maxIndex) {
        this.keys = new long[shortKeys.length];
        for(int i=minIndex; i<=maxIndex; i++) {
            this.keys[i] = shortKeys[i];
        }
    }

    @Override
    public long getKey(int index) {
        return keys[index];
    }

    @Override
    public int size() {
        return keys.length;
    }

    @Override
    FixedLengthKeys concat(List<FixedLengthKeys> keysList) {
        long[] concatKeys = new long[keys.length * keysList.size()];
        for(int i=0; i<keysList.size(); i++) {
            FixedLengthKeys element = keysList.get(i);
            element.copyTo(concatKeys, i * keys.length);
        }
        return new SmallLongArrayFixedLengthKeys(concatKeys);
    }

    @Override
    void copyTo(short[] array, int from) {
        throw new UnsupportedOperationException();
    }

    @Override
    void copyTo(int[] array, int from) {
        throw new UnsupportedOperationException();
    }
}
