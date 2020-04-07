package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonparser.LarJsonValueParser;

public class PrimitiveShortPropertyMapper extends ShortPropertyMapper {

    public PrimitiveShortPropertyMapper(int index, LarJsonValueParser larJsonValueParser) {
        super(index, larJsonValueParser);
    }

    @Override
    Short nullValue() {
        return 0;
    }
}
