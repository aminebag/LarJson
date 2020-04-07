package com.aminebag.larjson.jsonmapper.keys;

import java.util.List;

abstract class IntegerArrayFixedLengthKeys extends FixedLengthKeys {
    final int[] keys;

    IntegerArrayFixedLengthKeys(int length) {
        this.keys = new int[length];
    }

    IntegerArrayFixedLengthKeys(int[] keys) {
        this.keys = keys;
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
        int[] concatKeys = new int[keys.length * keysList.size()];
        for(int i=0; i<keysList.size(); i++) {
            FixedLengthKeys element = keysList.get(i);
            element.copyTo(concatKeys, i * keys.length);
        }
        return new SmallIntegerArrayFixedLengthKeys(concatKeys);
    }

    @Override
    void copyTo(short[] array, int from) {
        throw new UnsupportedOperationException();
    }
}
