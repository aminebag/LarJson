package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.element.LarJsonContext;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for model properties that can be cached
 */
abstract class CacheableValuePropertyMapper<T> extends SimpleValuePropertyMapper<T> {

    public CacheableValuePropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                        int setterIndex, boolean required) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
    }

    @Override
    protected final T calculateValue(LarJsonContext context, long key, long parentJsonPosition)
            throws IOException, LarJsonException {
        long jsonPosition = parentJsonPosition + key;
        return context.getCachedValue(jsonPosition, ()->calculateCacheableValue(context, jsonPosition));
    }

    protected abstract T calculateCacheableValue(LarJsonContext context, long jsonPosition)
            throws IOException, LarJsonException;
}
