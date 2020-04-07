package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonparser.LarJsonValueParser;

public class PrimitiveBytePropertyMapper extends BytePropertyMapper {

    public PrimitiveBytePropertyMapper(int index) {
        super(index);
    }

    @Override
    Byte nullValue() {
        return 0;
    }
}
