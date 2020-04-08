package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.jsonmapper.configuration.LarJsonConfiguration;
import com.aminebag.larjson.jsonmapper.propertymapper.LarJsonPropertyMapper;
import com.aminebag.larjson.jsonmapper.keys.LarJsonKeys;
import com.aminebag.larjson.stream.ChannelCharacterStreamPool;
import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class LarJsonRootObject extends LarJsonInvocationHandler implements LarJsonRoot {

    private final LarJsonKeys keys;
    private final List<List<?>> lists;
    private final LarJsonConfiguration configuration;
    private final ChannelCharacterStreamPool characterStreamPool;

    public LarJsonRootObject(Map<Method, LarJsonPropertyMapper> getters, LarJsonKeys keys, List<List<?>> lists,
                             LarJsonConfiguration configuration,
                             ChannelCharacterStreamPool characterStreamPool) {
        super(getters);
        this.keys = keys;
        this.lists = lists;
        this.configuration = configuration;
        this.characterStreamPool = characterStreamPool;
    }

    @Override
    public CharacterStream getCharacterStream(long position) throws IOException {
        return characterStreamPool.getCharacterStream(position);
    }

    @Override
    public void onClose() throws IOException {
        close();
    }

    @Override
    public long getKey(int index) {
        return keys.getKey(index);
    }

    @Override
    public List<?> getList(int index) {
        return lists.get(index);
    }

    @Override
    public int getTopObjectOffset() {
        return 0;
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
