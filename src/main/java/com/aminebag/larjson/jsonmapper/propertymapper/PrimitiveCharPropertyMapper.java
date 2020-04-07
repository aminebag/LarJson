package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonparser.LarJsonValueParser;

public class PrimitiveCharPropertyMapper extends CharPropertyMapper {

    public PrimitiveCharPropertyMapper(int index, LarJsonValueParser larJsonValueParser) {
        super(index, larJsonValueParser);
    }

    @Override
    Character nullValue() {
        return 0;
    }
}
