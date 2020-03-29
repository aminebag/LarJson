package com.aminebag.larjson.mapper.propertymapper;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for boolean model properties
 */
public class PrimitiveBooleanPropertyMapper extends BooleanPropertyMapper {

    public PrimitiveBooleanPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                          int setterIndex, boolean required) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
    }

    @Override
    public Boolean nullValue() {
        return false;
    }

    @Override
    public Class<Boolean> getType() {
        return boolean.class;
    }
}
