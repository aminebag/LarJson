package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.exception.LarJsonCheckedException;
import com.aminebag.larjson.jsonmapper.LarJsonBranch;

import java.io.IOException;

public abstract class LarJsonPropertyMapper<T> {
    public static final int NULL_VALUE = 0;

    private final int index;

    public LarJsonPropertyMapper(int index) {
        this.index = index;
    }

    public abstract T calculateValue(LarJsonBranch branch, int topObjectOffset)
            throws IOException, LarJsonCheckedException;

    public abstract int getLength();

    final long getKey(LarJsonBranch branch, int topObjectOffset) {
        return branch.getKey(topObjectOffset + index);
    }

    final T illegalKey(long key){
        throw new IllegalArgumentException("Key not supported : " + key);
    }

    public int getIndex() {
        return index;
    }
}
