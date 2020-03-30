package com.aminebag.larjson.jsonparser;

import com.aminebag.larjson.stream.ByteStream;
import com.aminebag.larjson.stringdecoder.CharacterDecoder;

/**
 * Credits : This code is inspired, to a great degree, by the class JsonReader of the Gson library
 */
public class GsonTokenParser implements LarJsonTokenParser{

    private final ByteStream byteStream;
    private final CharacterDecoder characterDecoder;
    private final boolean lenient;

    public GsonTokenParser(ByteStream byteStream, CharacterDecoder characterDecoder, boolean lenient) {
        this.byteStream = byteStream;
        this.characterDecoder = characterDecoder;
        this.lenient = lenient;
    }

    @Override
    public LarJsonToken peek() {
        return null;
    }

    @Override
    public void beginArray() {

    }

    @Override
    public void beginObject() {

    }

    @Override
    public void endArray() {

    }

    @Override
    public void endObject() {

    }

    @Override
    public boolean nextBoolean() {
        return false;
    }

    @Override
    public double nextDouble() {
        return 0;
    }

    @Override
    public int nextInt() {
        return 0;
    }

    @Override
    public long extLong() {
        return 0;
    }

    @Override
    public String nextName() {
        return null;
    }

    @Override
    public void nextNull() {

    }

    @Override
    public String nextString() {
        return null;
    }

    @Override
    public void skipValue() {

    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }
}
