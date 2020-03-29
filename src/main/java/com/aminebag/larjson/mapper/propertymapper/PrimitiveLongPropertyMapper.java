package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.parser.LarJsonValueParser;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for long model properties
 */
public class PrimitiveLongPropertyMapper extends LongPropertyMapper {

    public PrimitiveLongPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                       int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, larJsonValueParser);
    }

    @Override
    public Long nullValue() {
        return 0L;
    }

    @Override
    public Class<Long> getType() {
        return long.class;
    }
}
