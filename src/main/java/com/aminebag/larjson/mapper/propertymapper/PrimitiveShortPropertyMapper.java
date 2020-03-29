package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.parser.LarJsonValueParser;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for short model properties
 */
public class PrimitiveShortPropertyMapper extends ShortPropertyMapper {

    public PrimitiveShortPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                        int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, larJsonValueParser);
    }

    @Override
    public Short nullValue() {
        return 0;
    }

    @Override
    public Class<Short> getType() {
        return short.class;
    }
}
