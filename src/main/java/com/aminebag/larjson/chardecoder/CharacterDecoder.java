package com.aminebag.larjson.chardecoder;

import com.aminebag.larjson.stream.ByteStream;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A decoder of characters based on stream of bytes
 */
public interface CharacterDecoder {

    /**
     * Decodes a single character from the provide byte stream. Must not read a byte from the character that follows
     * @return decoded character or -1 if end of stream
     */
    int decodeCharacter(ByteStream byteStream) throws CharacterDecodingException, IOException;
}