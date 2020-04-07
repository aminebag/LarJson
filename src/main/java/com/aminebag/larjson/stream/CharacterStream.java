package com.aminebag.larjson.stream;

import com.aminebag.larjson.stringdecoder.CharacterDecodingException;

import java.io.IOException;

public interface CharacterStream {
    public char nextChar() throws CharacterDecodingException, IOException;
    public boolean hasNext() throws CharacterDecodingException, IOException;
}
