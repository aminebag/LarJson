package com.aminebag.larjson.jsonmapper.keys;

public abstract class DynamicLengthKeys extends LarJsonKeys {
    DynamicLengthKeys() {
    }

    public abstract DynamicLengthKeys add(long key);

    int calculateNewLength(int currentLength, int minLength) {
        return Math.max(currentLength + currentLength >> 1, minLength);
    }
}
