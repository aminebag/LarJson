package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.parser.LarJsonValueParser;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for int model properties
 */
public class PrimitiveIntegerPropertyMapper extends IntegerPropertyMapper {

    public PrimitiveIntegerPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                          int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, larJsonValueParser);
    }

    @Override
    public Integer nullValue() {
        return 0;
    }

    @Override
    public Class<Integer> getType() {
        return int.class;
    }
}
