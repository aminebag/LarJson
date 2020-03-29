package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.parser.LarJsonValueParser;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for byte model properties
 */
public class PrimitiveBytePropertyMapper extends BytePropertyMapper {

    public PrimitiveBytePropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                       int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, larJsonValueParser);
    }

    @Override
    public Byte nullValue() {
        return 0;
    }

    @Override
    public Class<Byte> getType() {
        return byte.class;
    }
}
