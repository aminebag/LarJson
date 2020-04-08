package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.jsonmapper.configuration.LarJsonConfiguration;
import com.aminebag.larjson.jsonmapper.propertymapper.LarJsonPropertyMapper;
import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class LarJsonObject extends LarJsonInvocationHandler {

    private final LarJsonRoot root;
    private final LarJsonBranch branch;
    private final int topObjectOffset;

    public LarJsonObject(LarJsonBranch branch, Map<Method, LarJsonPropertyMapper> getters, int topObjectOffset) {
        super(getters);
        this.root = branch.getRoot();
        this.branch = branch;
        this.topObjectOffset = topObjectOffset;
    }

    @Override
    public CharacterStream getCharacterStream(long position) throws IOException {
        return root.getCharacterStream(position);
    }

    @Override
    public long getKey(int index) {
        return branch.getKey(index);
    }

    @Override
    public List<?> getList(int index) {
        return branch.getList(index);
    }

    @Override
    public int getTopObjectOffset() {
        return topObjectOffset;
    }

    @Override
    public LarJsonRoot getRoot() {
        return root;
    }

    @Override
    LarJsonConfiguration getConfiguration() {
        return root.getConfiguration();
    }
}
