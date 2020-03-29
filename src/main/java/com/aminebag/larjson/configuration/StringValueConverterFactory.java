package com.aminebag.larjson.configuration;

import com.aminebag.larjson.valueconverter.StringValueConverter;

/**
 * @author Amine Bagdouri
 *
 * A factory of {@link StringValueConverter}
 */
public interface StringValueConverterFactory {

    /**
     * @param clazz the converter's type
     * @return a converter corresponding to the given class
     */
    <T> StringValueConverter<T> get(Class<T> clazz);
}
