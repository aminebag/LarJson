package com.aminebag.larjson.jsonmapper.getter;

import com.aminebag.larjson.jsonmapper.LarJsonRoot;

import java.lang.reflect.Method;

public class StringGetter extends LarJsonGetter<String> {
    public StringGetter(int index) {
        super(index);
    }

    @Override
    public String calculateValue(Method method, LarJsonRoot root, int rootObjectOffset) {
        return null;
    }
}
