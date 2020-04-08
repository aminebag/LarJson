package com.aminebag.larjson.jsonmapper.configuration;

import com.aminebag.larjson.jsonmapper.stringconverter.StringConverter;

public interface StringConverterFactory {
    StringConverter<?> getStringConverter(Class<?> clazz);
}
