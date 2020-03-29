package com.aminebag.larjson.configuration;

import com.aminebag.larjson.chardecoder.CharacterDecoder;
import com.aminebag.larjson.parser.LarJsonTokenParser;
import com.aminebag.larjson.stream.ByteStream;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A factory of {@link LarJsonTokenParser}
 */
public interface LarJsonTokenParserFactory {

    /**
     * @return an instance of {@link LarJsonTokenParser} based on the provided arguments
     * @throws IOException if a resource access error occurs
     */
    LarJsonTokenParser get(ByteStream byteStream, CharacterDecoder characterDecoder,
                           LarJsonReadConfiguration configuration) throws IOException;
}
