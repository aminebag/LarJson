package com.aminebag.larjson.parser.getter;

import com.aminebag.larjson.parser.LarJsonRoot;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;

public class BigEnumLarJsonGetter extends LarJsonGetter<Enum<?>> {
    public BigEnumLarJsonGetter(int index) {
        super(index);
    }

    @Override
    public Enum<?> calculateValue(Method method, LarJsonRoot root, int rootObjectOffset) {

        return null;
    }
}
