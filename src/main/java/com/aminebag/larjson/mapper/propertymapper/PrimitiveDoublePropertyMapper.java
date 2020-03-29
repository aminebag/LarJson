package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.parser.LarJsonValueParser;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for double model properties
 */
public class PrimitiveDoublePropertyMapper extends DoublePropertyMapper {

    public PrimitiveDoublePropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                         int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, larJsonValueParser);
    }

    @Override
    public Double nullValue() {
        return 0d;
    }

    @Override
    public Class<Double> getType() {
        return double.class;
    }
}
