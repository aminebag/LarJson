package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonmapper.LarJsonBranch;
import com.aminebag.larjson.jsonparser.LarJsonParseException;
import com.aminebag.larjson.jsonparser.LarJsonToken;
import com.aminebag.larjson.jsonparser.LarJsonTokenParser;
import com.aminebag.larjson.jsonparser.LarJsonValueParser;

import java.io.IOException;

public class BytePropertyMapper extends SimpleValuePropertyMapper<Byte> {

    private static final long SHIFT = Byte.MAX_VALUE + 2;
    private static final long MAX_VALUE = SHIFT + Byte.MAX_VALUE - Byte.MIN_VALUE;

    public BytePropertyMapper(int index) {
        super(index);
    }

    @Override
    Byte calculateValue(LarJsonBranch branch, long key) {
        if(key > MAX_VALUE) {
            return illegalKey(key);
        } else {
            return (byte)(key - SHIFT);
        }
    }

    @Override
    long calculateKey(LarJsonToken token, LarJsonTokenParser parser, long position)
            throws IOException, LarJsonParseException {

        byte value = parser.nextByte();
        return value + SHIFT;
    }
}
