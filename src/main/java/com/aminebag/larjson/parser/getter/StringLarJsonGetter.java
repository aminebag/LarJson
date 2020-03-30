package com.aminebag.larjson.parser.getter;

import com.aminebag.larjson.parser.LarJsonRoot;

import java.lang.reflect.Method;

public class StringLarJsonGetter extends LarJsonGetter<String> {
    public StringLarJsonGetter(int index) {
        super(index);
    }

    @Override
    public String calculateValue(Method method, LarJsonRoot root, int rootObjectOffset) {
        return null;
    }
}
