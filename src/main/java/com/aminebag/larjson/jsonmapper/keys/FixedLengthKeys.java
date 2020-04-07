package com.aminebag.larjson.jsonmapper.keys;

import java.util.List;

public abstract class FixedLengthKeys extends LarJsonKeys {
    FixedLengthKeys() {
    }

    public abstract FixedLengthKeys setKey(int index, long key);
    abstract FixedLengthKeys reproduce();
    abstract FixedLengthKeys concat(List<FixedLengthKeys> keysList);
    abstract void copyTo(short[] array, int from);
    abstract void copyTo(int[] array, int from);
    abstract void copyTo(long[] array, int from);
}
