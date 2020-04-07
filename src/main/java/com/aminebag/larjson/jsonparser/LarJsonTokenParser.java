package com.aminebag.larjson.jsonparser;

import java.io.IOException;

/**
 * A stateful parser that can be used to parse a JSON object or array
 * Credits : The contract of this interface is inspired, to a great degree, by the class JsonReader of the Gson library
 */
public interface LarJsonTokenParser {

    LarJsonToken peek() throws LarJsonParseException, IOException;
    void beginArray() throws LarJsonParseException, IOException;
    void beginObject() throws LarJsonParseException, IOException;
    void endArray() throws LarJsonParseException, IOException;
    void endObject() throws LarJsonParseException, IOException;
    boolean nextBoolean() throws LarJsonParseException, IOException;
    double nextDouble() throws LarJsonParseException, IOException;
    byte nextByte() throws LarJsonParseException, IOException;
    short nextShort() throws LarJsonParseException, IOException;
    int nextInt() throws LarJsonParseException, IOException;
    long nextLong() throws LarJsonParseException, IOException;
    char nextChar() throws LarJsonParseException, IOException;
    String nextName() throws LarJsonParseException, IOException;
    void nextNull() throws LarJsonParseException, IOException;
    String nextString() throws LarJsonParseException, IOException;
    void skipValue() throws LarJsonParseException, IOException;

    /**
     * @return the current byte position
     */
    long getCurrentPosition() throws LarJsonParseException, IOException;
}
