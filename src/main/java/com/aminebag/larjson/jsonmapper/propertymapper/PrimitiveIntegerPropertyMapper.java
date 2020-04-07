package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonparser.LarJsonValueParser;

public class PrimitiveIntegerPropertyMapper extends IntegerPropertyMapper {

    public PrimitiveIntegerPropertyMapper(int index, LarJsonValueParser larJsonValueParser) {
        super(index, larJsonValueParser);
    }

    @Override
    Integer nullValue() {
        return 0;
    }
}
