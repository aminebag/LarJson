package com.aminebag.larjson.parser.getter;

import com.aminebag.larjson.parser.LarJsonRoot;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;

public abstract class LarJsonGetter<T> {
    public static final int NULL_VALUE = -1;

    private final int index;

    public LarJsonGetter(int index) {
        this.index = index;
    }

    public abstract T calculateValue(Method method, LarJsonRoot root, int rootObjectOffset);

    protected final long getKey(LarJsonRoot root, int rootObjectOffset) {
        return root.getKey(rootObjectOffset + index);
    }

    protected final T illegalKey(long key){
        throw new IllegalArgumentException("Key not supported : "+key);
    }
}
