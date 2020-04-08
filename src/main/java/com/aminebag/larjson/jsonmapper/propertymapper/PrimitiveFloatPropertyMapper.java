package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonparser.LarJsonValueParser;

public class PrimitiveFloatPropertyMapper extends FloatPropertyMapper {

    public PrimitiveFloatPropertyMapper(int index, LarJsonValueParser larJsonValueParser) {
        super(index, larJsonValueParser);
    }

    @Override
    Float nullValue() {
        return 0f;
    }
}
