package com.aminebag.larjson.jsonmapper.getter;

import com.aminebag.larjson.jsonmapper.LarJsonRoot;

import java.lang.reflect.Method;

public class SmallEnumGetter extends LarJsonGetter<Enum<?>> {
    public static final int MAX_VALUES = Short.MAX_VALUE;
    private final Enum[] values;

    public SmallEnumGetter(int index, Enum[] values) {
        super(index);
        this.values = values;
    }

    @Override
    public Enum<?> calculateValue(Method method, LarJsonRoot root, int rootObjectOffset) {
        long key = getKey(root, rootObjectOffset);
        if(key == NULL_VALUE) {
            return null;
        } else if (key < NULL_VALUE || key > values.length) {
            return illegalKey(key);
        } else {
            return values[(int)key - 1];
        }
    }
}
