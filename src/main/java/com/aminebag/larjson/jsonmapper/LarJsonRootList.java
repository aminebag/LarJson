package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.jsonmapper.configuration.LarJsonConfiguration;
import com.aminebag.larjson.jsonmapper.propertymapper.ObjectLarJsonPropertyMapper;
import com.aminebag.larjson.jsonmapper.keys.LarJsonKeys;
import com.aminebag.larjson.stream.ChannelCharacterStreamPool;
import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;
import java.util.List;

public class LarJsonRootList<T> extends AbstractLarJsonObjectList<T> implements LarJsonRoot, CloseableList<T> {

    private final LarJsonConfiguration configuration;
    private final ChannelCharacterStreamPool characterStreamPool;

    public LarJsonRootList(ObjectLarJsonPropertyMapper getter, LarJsonKeys keys, List<List<?>> lists,
                           LarJsonConfiguration configuration,
                           ChannelCharacterStreamPool characterStreamPool) {
        super(keys, getter, lists);
        this.configuration = configuration;
        this.characterStreamPool = characterStreamPool;
    }

    @Override
    public CharacterStream getCharacterStream(long position) throws IOException {
        return characterStreamPool.getCharacterStream(position);
    }

    @Override
    public LarJsonRoot getRoot() {
        return this;
    }

    @Override
    public LarJsonConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void close() throws IOException {
        characterStreamPool.close();
    }
}
