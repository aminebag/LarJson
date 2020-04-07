package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonparser.LarJsonValueParser;

public class PrimitiveLongPropertyMapper extends LongPropertyMapper {

    public PrimitiveLongPropertyMapper(int index, LarJsonValueParser larJsonValueParser) {
        super(index, larJsonValueParser);
    }

    @Override
    Long nullValue() {
        return 0L;
    }
}
