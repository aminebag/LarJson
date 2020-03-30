package com.aminebag.larjson.parser.getter;

import com.aminebag.larjson.parser.LarJsonRoot;

import java.io.RandomAccessFile;
import java.lang.reflect.Method;

public class BooleanLarJsonGetter extends LarJsonGetter<Boolean> {
    public static final int FALSE_VALUE = 0;
    public static final int TRUE_VALUE = 1;

    public BooleanLarJsonGetter(int index) {
        super(index);
    }

    @Override
    public Boolean calculateValue(Method method, LarJsonRoot root, int rootObjectOffset) {
        long key = getKey(root, rootObjectOffset);
        switch ((int)key) {
            case NULL_VALUE : return null;
            case FALSE_VALUE : return false;
            case TRUE_VALUE : return true;
        }
        return illegalKey(key);
    }
}
