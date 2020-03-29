package com.aminebag.larjson.valueconverter;

import com.aminebag.larjson.exception.LarJsonConversionException;

/**
 * @author Amine Bagdouri
 *
 * Indicates how to a convert objects of a certain type from/to {@link String}
 */
public interface StringValueConverter<T> {

    /**
     * Converts a String to an object of type {@code T}
     * @param s the String to convert, may be {@code null}
     * @return the converted object
     * @throws LarJsonConversionException if the given {@link String} is not convertable to {@code T}
     */
    T fromString(String s) throws LarJsonConversionException;

    /**
     * Converts an object of type {@code T} to String
     * @param value is not {@code null}
     * @return the converted string
     */
    String toString(T value);
}
