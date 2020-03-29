package com.aminebag.larjson.stream;

import com.aminebag.larjson.chardecoder.CharacterDecodingException;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A stream of characters
 */
public interface CharacterStream {
    int next() throws CharacterDecodingException, IOException;
    long getBytePosition() throws IOException;
}
