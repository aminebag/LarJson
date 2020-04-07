package com.aminebag.larjson.jsonmapper.propertymapper;

public class PrimitiveBooleanPropertyMapper extends BooleanPropertyMapper {

    public PrimitiveBooleanPropertyMapper(int index) {
        super(index);
    }

    @Override
    Boolean nullValue() {
        return false;
    }
}
