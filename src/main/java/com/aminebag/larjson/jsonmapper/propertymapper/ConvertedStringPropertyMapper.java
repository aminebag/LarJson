package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.exception.LarJsonCheckedException;
import com.aminebag.larjson.jsonmapper.LarJsonBranch;
import com.aminebag.larjson.jsonmapper.stringconverter.StringConverter;
import com.aminebag.larjson.jsonparser.LarJsonParseException;
import com.aminebag.larjson.jsonparser.LarJsonToken;
import com.aminebag.larjson.jsonparser.LarJsonTokenParser;
import com.aminebag.larjson.jsonparser.LarJsonValueParser;

import java.io.IOException;

public class ConvertedStringPropertyMapper<T> extends SimpleValuePropertyMapper<T> {
    private final LarJsonValueParser larJsonValueParser;
    private final StringConverter<T> stringConverter;

    public ConvertedStringPropertyMapper(int index,
                                         LarJsonValueParser larJsonValueParser,
                                         StringConverter<T> stringConverter) {
        super(index);
        this.larJsonValueParser = larJsonValueParser;
        this.stringConverter = stringConverter;
    }

    @Override
    T calculateValue(LarJsonBranch branch, long key) throws IOException, LarJsonCheckedException {
        return stringConverter.read(larJsonValueParser.parseString(branch.getCharacterStream(key)));
    }

    @Override
    T nullValue() throws LarJsonCheckedException {
        return stringConverter.read(null);
    }

    @Override
    long calculateKey(LarJsonToken token, LarJsonTokenParser parser, long position)
            throws IOException, LarJsonParseException {

        parser.skipValue();
        return position;
    }
}
