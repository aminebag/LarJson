package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonparser.LarJsonValueParser;

public class PrimitiveDoublePropertyMapper extends DoublePropertyMapper {

    public PrimitiveDoublePropertyMapper(int index, LarJsonValueParser larJsonValueParser) {
        super(index, larJsonValueParser);
    }

    @Override
    Double nullValue() {
        return 0d;
    }
}
