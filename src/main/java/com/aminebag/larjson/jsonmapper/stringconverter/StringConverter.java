package com.aminebag.larjson.jsonmapper.stringconverter;

import com.aminebag.larjson.jsonmapper.exception.LarJsonConversionException;

public interface StringConverter<T> {

    /**
     *
     * @param s the String to convert, may be {@code null}
     * @return the converted object
     * @throws LarJsonConversionException
     */
    T read(String s) throws LarJsonConversionException;
}
