package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.parser.LarJsonValueParser;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for char model properties
 */
public class PrimitiveCharPropertyMapper extends CharPropertyMapper {

    public PrimitiveCharPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                       int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, larJsonValueParser);
    }

    @Override
    public Character nullValue() {
        return 0;
    }

    @Override
    public Class<Character> getType() {
        return char.class;
    }
}
