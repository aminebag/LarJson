package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.parser.LarJsonValueParser;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for float model properties
 */
public class PrimitiveFloatPropertyMapper extends FloatPropertyMapper {

    public PrimitiveFloatPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                        int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, larJsonValueParser);
    }

    @Override
    public Float nullValue() {
        return 0f;
    }

    @Override
    public Class<Float> getType() {
        return float.class;
    }
}
