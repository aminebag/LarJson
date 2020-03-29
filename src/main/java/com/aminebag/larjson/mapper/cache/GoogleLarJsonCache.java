package com.aminebag.larjson.mapper.cache;

import com.aminebag.larjson.exception.LarJsonException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * {@inheritDoc}
 */
class GoogleLarJsonCache extends LarJsonCache {

    private static final Object NULL = new Object();

    private final Cache<Long, Object> cache;

    GoogleLarJsonCache(int maxSize) {
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .build();
    }

    @Override
    public <T> T get(long jsonPosition, LarJsonValueCalculator<T> valueCalculator) throws IOException, LarJsonException {
        Object value = cache.getIfPresent(jsonPosition);
        if(value == null){
            value = valueCalculator.calculate();
            if(value == null) {
                cache.put(jsonPosition, NULL);
                return null;
            } else {
                cache.put(jsonPosition, value);
                return (T) value;
            }
        } else if(value == NULL) {
            return null;
        } else {
            return (T) value;
        }
    }
}
