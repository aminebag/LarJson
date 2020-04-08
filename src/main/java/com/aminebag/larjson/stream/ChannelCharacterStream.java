package com.aminebag.larjson.stream;

import com.aminebag.larjson.channel.RandomReadAccessChannel;
import com.aminebag.larjson.stringdecoder.CharacterDecoder;
import com.aminebag.larjson.stringdecoder.CharacterDecodingException;

import java.io.IOException;

public class ChannelCharacterStream implements CharacterStream {

    private static final int NULL = -1;
    private static final int EOF = -2;
    private final CharacterDecoder characterDecoder;
    private final ChannelByteStream byteStream;
    private int nextChar = NULL;

    public ChannelCharacterStream(
            RandomReadAccessChannel channel,
            CharacterDecoder characterDecoder) {
        this.byteStream = new ChannelByteStream(channel);
        this.characterDecoder = characterDecoder;
    }

    public void seek(long position) throws IOException {
        byteStream.seek(position);
        nextChar = NULL;
    }

    @Override
    public char nextChar() throws IOException, CharacterDecodingException {
        if(nextChar == EOF || (nextChar == NULL && !hasNext())){
            throw new IllegalStateException();
        }
        return (char)nextChar;
    }

    @Override
    public boolean hasNext() throws IOException, CharacterDecodingException {
        if(nextChar >= 0){
            return true;
        }
        if(nextChar == EOF){
            return false;
        }
        nextChar = characterDecoder.decodeCharacter(byteStream);
        if(nextChar >= 0){
            return true;
        } else {
            nextChar = EOF;
            return false;
        }
    }
}
