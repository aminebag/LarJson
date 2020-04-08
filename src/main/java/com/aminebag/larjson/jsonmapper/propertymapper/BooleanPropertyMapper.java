package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonmapper.LarJsonBranch;
import com.aminebag.larjson.jsonparser.LarJsonParseException;
import com.aminebag.larjson.jsonparser.LarJsonToken;
import com.aminebag.larjson.jsonparser.LarJsonTokenParser;

import java.io.IOException;

public class BooleanPropertyMapper extends SimpleValuePropertyMapper<Boolean> {
    public static final long FALSE_VALUE = 1;
    public static final long TRUE_VALUE = 2;

    public BooleanPropertyMapper(int index) {
        super(index);
    }

    @Override
    Boolean calculateValue(LarJsonBranch branch, long key) {
        if (key == FALSE_VALUE) {
            return false;
        } else if (key == TRUE_VALUE) {
            return true;
        } else {
            return illegalKey(key);
        }
    }

    @Override
    long calculateKey(LarJsonToken token, LarJsonTokenParser parser, long position)
            throws IOException, LarJsonParseException {
        boolean value = parser.nextBoolean();
        return value ? TRUE_VALUE : FALSE_VALUE;
    }
}
