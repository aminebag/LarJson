package com.aminebag.larjson.stringdecoder;

import com.aminebag.larjson.ByteStream;

public interface StringDecoder {

    /**
     * @return decoded character or -1 if end of stream
     */
    int decodeCharacter(ByteStream byteStream) throws StringDecodingException;
}