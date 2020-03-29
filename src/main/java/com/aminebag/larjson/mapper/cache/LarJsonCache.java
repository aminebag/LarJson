package com.aminebag.larjson.mapper.cache;

import com.aminebag.larjson.exception.LarJsonException;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A cache used to store the most recently used LarJson values (objects, lists and simple values).
 */
public abstract class LarJsonCache {

    private static final LarJsonCache EMPTY = new LarJsonCache() {
        @Override
        public <T> T get(long jsonPosition, LarJsonValueCalculator<T> valueCalculator)
                throws IOException, LarJsonException {
            return valueCalculator.calculate();
        }
    };

    public abstract <T> T get(long jsonPosition, LarJsonValueCalculator<T> valueCalculator)
            throws IOException, LarJsonException;

    public static LarJsonCache get(int cacheSize) {
        return cacheSize <= 0 ? EMPTY : new GoogleLarJsonCache(cacheSize);
    }
}
