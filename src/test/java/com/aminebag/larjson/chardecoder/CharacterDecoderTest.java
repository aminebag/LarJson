package com.aminebag.larjson.chardecoder;

import com.aminebag.larjson.stream.ArrayByteStream;
import com.aminebag.larjson.stream.ByteStream;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Amine Bagdouri
 */
public abstract class CharacterDecoderTest {

    public static final String ASCII_STRING = "the brown fox jumped over the l@zy Dog : " +
            "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\t\n" +
            "\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D" +
            "\u001E\u001F !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`" +
            "abcdefghijklmnopqrstuvwxyz{|}~\u007F";

    protected void testString(String expected) throws CharacterDecodingException, IOException {
        ByteStream byteStreamMock = new ArrayByteStream(expected, charset());
        StringBuilder builder = new StringBuilder();
        int c;
        while (true) {
            c = decoder().decodeCharacter(byteStreamMock);
            if (c == -1) {
                break;
            }
            builder.append((char) c);
        }
        assertTrue(expected.equals(builder.toString()));
    }

    abstract protected CharacterDecoder decoder();
    abstract protected Charset charset();
}
