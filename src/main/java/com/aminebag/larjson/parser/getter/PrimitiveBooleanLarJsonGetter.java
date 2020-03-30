package com.aminebag.larjson.parser.getter;

import com.aminebag.larjson.parser.LarJsonRoot;

import java.io.RandomAccessFile;
import java.lang.reflect.Method;

public class PrimitiveBooleanLarJsonGetter extends LarJsonGetter<Boolean> {
    public static final int NULL_VALUE = 0;
    public static final int FALSE_VALUE = 1;
    public static final int TRUE_VALUE = 2;

    public PrimitiveBooleanLarJsonGetter(int index) {
        super(index);
    }

    @Override
    public Boolean calculateValue(Method method, LarJsonRoot root, int rootObjectOffset) {
        long key = getKey(root, rootObjectOffset);
        switch ((int)key) {
            case NULL_VALUE :
            case FALSE_VALUE : return false;
            case TRUE_VALUE : return true;
        }
        return illegalKey(key);
    }
}
