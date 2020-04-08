package com.aminebag.larjson.jsonmapper.keys;

import java.util.List;

abstract class ShortArrayFixedLengthKeys extends FixedLengthKeys {
    final short[] keys;

    ShortArrayFixedLengthKeys(int length){
        this.keys = new short[length];
    }

    ShortArrayFixedLengthKeys(short[] keys){
        this.keys = keys;
    }

    @Override
    public final long getKey(int index) {
        return keys[index];
    }

    @Override
    public int size() {
        return keys.length;
    }

    @Override
    final FixedLengthKeys concat(List<FixedLengthKeys> keysList) {
        short[] concatKeys = new short[keys.length * keysList.size()];
        for(int i=0; i<keysList.size(); i++) {
            FixedLengthKeys element = keysList.get(i);
            element.copyTo(concatKeys, i * keys.length);
        }
        return new SmallShortArrayFixedLengthKeys(concatKeys);
    }
}
