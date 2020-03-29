package com.aminebag.larjson.stream;

import com.aminebag.larjson.channel.RandomReadAccessChannel;
import com.aminebag.larjson.chardecoder.CharacterDecoder;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.configuration.RandomReadAccessChannelFactory;
import com.aminebag.larjson.resource.ResourceFactory;
import com.aminebag.larjson.resource.ResourcePool;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A pool of {@link ChannelCharacterStream}s
 */
public class ChannelCharacterStreamPool implements Closeable {

    private final ResourcePool<ChannelCharacterStream> characterStreamPool;

    public ChannelCharacterStreamPool(RandomReadAccessChannelFactory channelFactory,
                                      CharacterDecoder characterDecoder,
                                      LarJsonTypedReadConfiguration configuration) {
        characterStreamPool = new ResourcePool<>(new ResourceFactory<ChannelCharacterStream>() {
            @Override
            public ChannelCharacterStream create() throws IOException {
                RandomReadAccessChannel channel = channelFactory.get(configuration);
                return new ChannelCharacterStream(channel, characterDecoder);
            }

            @Override
            public void close() throws IOException {
                //do nothing
            }
        });
    }

    public CharacterStream getCharacterStream(long position) throws IOException {
        ChannelCharacterStream characterStream = characterStreamPool.get();
        characterStream.seek(position);
        return characterStream;
    }

    @Override
    public void close() throws IOException {
        characterStreamPool.close();
    }
}
