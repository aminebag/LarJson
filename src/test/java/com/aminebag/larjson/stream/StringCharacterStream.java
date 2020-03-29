package com.aminebag.larjson.stream;

import com.aminebag.larjson.chardecoder.CharacterDecodingException;

import java.io.IOException;

public class StringCharacterStream implements CharacterStream {

    private final String str;
    private int position = 0;

    public StringCharacterStream(String str) {
        this.str = str;
    }

    @Override
    public int next() throws CharacterDecodingException, IOException {
        if(position >= str.length()) {
            return -1;
        }
        return str.charAt(position++);
    }

    @Override
    public long getBytePosition() throws IOException {
        return position;
    }
}
