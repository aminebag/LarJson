package com.aminebag.larjson.stream;

import com.aminebag.larjson.channel.RandomReadAccessChannel;
import com.aminebag.larjson.jsonmapper.exception.MultipleResourcesIOException;
import com.aminebag.larjson.stringdecoder.CharacterDecoder;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ChannelCharacterStreamPool implements Closeable {

    private final Set<RandomReadAccessChannel> allOpenChannels = ConcurrentHashMap.newKeySet();
    private ThreadLocal<ChannelCharacterStream> characterStreamThreadLocal;

    public ChannelCharacterStreamPool(Supplier<RandomReadAccessChannel> channelFactory,
                                      CharacterDecoder characterDecoder) {

        this.characterStreamThreadLocal = ThreadLocal.withInitial(()->{
            RandomReadAccessChannel channel = channelFactory.get();
            allOpenChannels.add(channel);
            return new ChannelCharacterStream(channel, characterDecoder);
        });
    }

    public CharacterStream getCharacterStream(long position) throws IOException {
        synchronized (characterStreamThreadLocal){
            if(characterStreamThreadLocal != null) {
                ChannelCharacterStream stream = characterStreamThreadLocal.get();
                stream.seek(position);
                return stream;
            } else {
                throw new IOException("Channel was closed");
            }
        }
    }

    @Override
    public void close() throws IOException {
        List<RandomReadAccessChannel> channels;
        synchronized (characterStreamThreadLocal){
            if(characterStreamThreadLocal == null){
                return;
            }
            characterStreamThreadLocal = null;
            channels = new ArrayList<>(allOpenChannels);
        }
        List<IOException> exceptions = new ArrayList<>();
        for(RandomReadAccessChannel channel : channels) {
            try {
                channel.close();
            } catch (IOException e) {
                exceptions.add(e);
            }
        }
        if(exceptions.size() > 1) {
            throw new MultipleResourcesIOException(exceptions);
        } else if (exceptions.size() == 1) {
            throw exceptions.get(0);
        }
    }
}
