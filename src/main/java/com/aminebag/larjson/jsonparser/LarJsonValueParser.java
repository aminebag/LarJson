package com.aminebag.larjson.jsonparser;

import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;

/**
 * A stateless parser that parses a simple JSON value
 */
public interface LarJsonValueParser {
    String parseString(CharacterStream characterStream) throws LarJsonParseException, IOException;
    byte parseByte(CharacterStream characterStream) throws LarJsonParseException, IOException;
    short parseShort(CharacterStream characterStream) throws LarJsonParseException, IOException;
    int parseInt(CharacterStream characterStream) throws LarJsonParseException, IOException;
    float parseFloat(CharacterStream characterStream) throws LarJsonParseException, IOException;
    double parseDouble(CharacterStream characterStream) throws LarJsonParseException, IOException;
    char parseChar(CharacterStream characterStream) throws LarJsonParseException, IOException;
    boolean parseBoolean(CharacterStream characterStream) throws LarJsonParseException, IOException;
}
