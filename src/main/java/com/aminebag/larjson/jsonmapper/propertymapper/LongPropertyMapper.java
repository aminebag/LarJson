package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonmapper.LarJsonBranch;
import com.aminebag.larjson.jsonparser.LarJsonParseException;
import com.aminebag.larjson.jsonparser.LarJsonToken;
import com.aminebag.larjson.jsonparser.LarJsonTokenParser;
import com.aminebag.larjson.jsonparser.LarJsonValueParser;

import java.io.IOException;

public class LongPropertyMapper extends SimpleValuePropertyMapper<Long> {

    private final LarJsonValueParser larJsonValueParser;

    public LongPropertyMapper(int index, LarJsonValueParser larJsonValueParser) {
        super(index);
        this.larJsonValueParser = larJsonValueParser;
    }

    @Override
    Long calculateValue(LarJsonBranch branch, long key) throws IOException, LarJsonParseException {
        return larJsonValueParser.parseLong(branch.getCharacterStream(key));
    }

    @Override
    long calculateKey(LarJsonToken token, LarJsonTokenParser parser, long position)
            throws IOException, LarJsonParseException {

        parser.skipValue();
        return position;
    }
}
