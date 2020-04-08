package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.jsonmapper.propertymapper.LarJsonPropertyMapper;
import com.aminebag.larjson.jsonmapper.keys.LarJsonKeys;
import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;
import java.util.List;

public class LarJsonSimpleValueList<T> extends AbstractLarJsonList<T> {

    private final LarJsonRoot root;
    private final long branchPosition;

    public LarJsonSimpleValueList(LarJsonKeys keys, LarJsonPropertyMapper<T> getter,
                                  LarJsonRoot root, long branchPosition) {
        super(keys, getter);
        this.root = root;
        this.branchPosition = branchPosition;
    }

    @Override
    public CharacterStream getCharacterStream(long position) throws IOException {
        return root.getCharacterStream(branchPosition + position);
    }

    @Override
    public List<?> getList(int index) {
        return null;
    }

    @Override
    public LarJsonRoot getRoot() {
        return root;
    }
}
