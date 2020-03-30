package com.aminebag.larjson.parser.getter;

import com.aminebag.larjson.parser.LarJsonRoot;

import java.io.RandomAccessFile;
import java.lang.reflect.Method;

public class SmallEnumLarJsonGetter extends LarJsonGetter<Enum<?>> {
    public static final int MAX_VALUES = Short.MAX_VALUE + 1;
    private final Enum[] values;

    public SmallEnumLarJsonGetter(int index, Enum[] values) {
        super(index);
        this.values = values;
    }

    @Override
    public Enum<?> calculateValue(Method method, LarJsonRoot root, int rootObjectOffset) {
        long key = getKey(root, rootObjectOffset);
        if(key == NULL_VALUE) {
            return null;
        } else if (key < NULL_VALUE || key >= values.length) {
            return illegalKey(key);
        } else {
            return values[(int)key];
        }
    }
}
