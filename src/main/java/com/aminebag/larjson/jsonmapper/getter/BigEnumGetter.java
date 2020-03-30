package com.aminebag.larjson.jsonmapper.getter;

import com.aminebag.larjson.jsonmapper.LarJsonRoot;

import java.lang.reflect.Method;

public class BigEnumGetter extends LarJsonGetter<Enum<?>> {
    public BigEnumGetter(int index) {
        super(index);
    }

    @Override
    public Enum<?> calculateValue(Method method, LarJsonRoot root, int rootObjectOffset) {

        return null;
    }
}
