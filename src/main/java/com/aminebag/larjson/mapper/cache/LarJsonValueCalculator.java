package com.aminebag.larjson.mapper.cache;

import com.aminebag.larjson.exception.LarJsonException;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * Provides a way to calculate a LarJson value
 */
@FunctionalInterface
public interface LarJsonValueCalculator<T> {
    T calculate() throws IOException, LarJsonException;
}
