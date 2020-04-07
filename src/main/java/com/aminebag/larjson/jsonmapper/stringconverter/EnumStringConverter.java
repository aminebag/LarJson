package com.aminebag.larjson.jsonmapper.stringconverter;

import com.aminebag.larjson.jsonmapper.exception.LarJsonConversionException;

public class EnumStringConverter<T extends Enum<T>> implements StringConverter<T> {

    private final Class<T> clazz;

    public EnumStringConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T read(String s) throws LarJsonConversionException {
        if(s == null){
            return null;
        }
        try {
            return Enum.valueOf(clazz, s);
        }catch (IllegalArgumentException e){
            throw new LarJsonConversionException(e);
        }
    }
}
