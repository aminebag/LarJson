package com.aminebag.larjson.stringdecoder;

import com.aminebag.larjson.stream.ByteStream;

import java.io.IOException;

public interface CharacterDecoder {

    /**
     * @return decoded character or -1 if end of stream
     */
    int decodeCharacter(ByteStream byteStream) throws CharacterDecodingException, IOException;
}