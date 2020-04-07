package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonmapper.LarJsonBranch;
import com.aminebag.larjson.jsonmapper.LarJsonObject;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ObjectLarJsonPropertyMapper<T> extends LarJsonPropertyMapper<T> {

    private final Class<T> clazz;
    private final int length;
    private final int level;
    private final Map<Method, LarJsonPropertyMapper> propertyMappers;

    public ObjectLarJsonPropertyMapper(int index, Class<T> clazz, int length, int level,
                                       Map<Method, LarJsonPropertyMapper> propertyMappers) {
        super(index);
        this.clazz = clazz;
        this.level = level;
        this.length = length;
        this.propertyMappers = propertyMappers;
    }

    @Override
    public T calculateValue(LarJsonBranch branch, int topObjectOffset) {
        long key = getKey(branch, topObjectOffset);
        if (key <= NULL_VALUE && key + level >= NULL_VALUE) {
            return null;
        }
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new LarJsonObject(branch.getRoot(), propertyMappers, topObjectOffset));
    }

    @Override
    public int getLength() {
        return length;
    }

    public Map<Method, LarJsonPropertyMapper> getPropertyMappers() {
        return propertyMappers;
    }
}
