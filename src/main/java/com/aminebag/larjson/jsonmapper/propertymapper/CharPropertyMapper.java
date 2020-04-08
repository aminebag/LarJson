package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonmapper.LarJsonBranch;
import com.aminebag.larjson.jsonparser.LarJsonParseException;
import com.aminebag.larjson.jsonparser.LarJsonToken;
import com.aminebag.larjson.jsonparser.LarJsonTokenParser;
import com.aminebag.larjson.jsonparser.LarJsonValueParser;

import java.io.IOException;

public class CharPropertyMapper extends SimpleValuePropertyMapper<Character> {

    private final LarJsonValueParser larJsonValueParser;

    public CharPropertyMapper(int index, LarJsonValueParser larJsonValueParser) {
        super(index);
        this.larJsonValueParser = larJsonValueParser;
    }

    @Override
    Character calculateValue(LarJsonBranch branch, long key) throws IOException, LarJsonParseException {
        return larJsonValueParser.parseChar(branch.getCharacterStream(key));
    }

    @Override
    long calculateKey(LarJsonToken token, LarJsonTokenParser parser, long position)
            throws IOException, LarJsonParseException {

        parser.skipValue();
        return position;
    }
}
