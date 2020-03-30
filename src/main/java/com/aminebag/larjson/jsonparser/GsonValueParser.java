package com.aminebag.larjson.jsonparser;

import com.aminebag.larjson.stream.CharacterStream;

/**
 * Credits : This code is inspired, to a great degree, by the class JsonReader of the Gson library
 */
public class GsonValueParser implements LarJsonValueParser {
    private final boolean lenient;

    public GsonValueParser(boolean lenient) {
        this.lenient = lenient;
    }

    @Override
    public String parseString(CharacterStream characterStream) throws LarJsonParseException {
        return null;
    }

    @Override
    public byte parseByte(CharacterStream characterStream) throws LarJsonParseException {
        return 0;
    }

    @Override
    public short parseShort(CharacterStream characterStream) throws LarJsonParseException {
        return 0;
    }

    @Override
    public int parseInt(CharacterStream characterStream) throws LarJsonParseException {
        return 0;
    }

    @Override
    public float parseFloat(CharacterStream characterStream) throws LarJsonParseException {
        return 0;
    }

    @Override
    public double parseDouble(CharacterStream characterStream) throws LarJsonParseException {
        return 0;
    }

    @Override
    public char parseChar(CharacterStream characterStream) throws LarJsonParseException {
        return 0;
    }

    @Override
    public boolean parseBoolean(CharacterStream characterStream) throws LarJsonParseException {
        return false;
    }
}
