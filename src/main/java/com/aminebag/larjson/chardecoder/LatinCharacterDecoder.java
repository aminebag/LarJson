package com.aminebag.larjson.chardecoder;

import com.aminebag.larjson.stream.ByteStream;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A character decoder for the ISO/CEI 8859-1 encoding.
 */
public class LatinCharacterDecoder implements CharacterDecoder{

    private static final LatinCharacterDecoder INSTANCE = new LatinCharacterDecoder();

    @Override
    public int decodeCharacter(ByteStream byteStream) throws IOException {
        if(!byteStream.hasAtLeastRemainingBytes(1)) {
            return -1;
        }
        return byteStream.nextByte() & 0xff;
    }

    public static LatinCharacterDecoder getInstance() {
        return INSTANCE;
    }
}
