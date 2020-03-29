package com.aminebag.larjson.stream;

import com.aminebag.larjson.channel.RandomReadAccessChannel;
import com.aminebag.larjson.chardecoder.CharacterDecoder;
import com.aminebag.larjson.chardecoder.CharacterDecodingException;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * {@inheritDoc}
 */
public class ChannelCharacterStream implements CharacterStream, Closeable {

    private final CharacterDecoder characterDecoder;
    private final ChannelByteStream byteStream;
    private final RandomReadAccessChannel channel;
    private long position = 0L;

    ChannelCharacterStream(
            RandomReadAccessChannel channel,
            CharacterDecoder characterDecoder) {
        this.channel = channel;
        this.byteStream = new ChannelByteStream(channel);
        this.characterDecoder = characterDecoder;
    }

    void seek(long position) throws IOException {
        byteStream.seek(this.position = position);
    }

    @Override
    public int next() throws IOException, CharacterDecodingException {
        position = byteStream.currentPosition();
        return characterDecoder.decodeCharacter(byteStream);
    }

    @Override
    public long getBytePosition() {
        return position;
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
