package com.aminebag.larjson.jsonmapper.getter;

import com.aminebag.larjson.jsonmapper.LarJsonRoot;

import java.lang.reflect.Method;

public class BooleanGetter extends LarJsonGetter<Boolean> {
    public static final int FALSE_VALUE = 1;
    public static final int TRUE_VALUE = 2;

    public BooleanGetter(int index) {
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
