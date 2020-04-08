package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.jsonmapper.propertymapper.ObjectLarJsonPropertyMapper;
import com.aminebag.larjson.jsonmapper.keys.LarJsonKeys;
import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;
import java.util.List;

public class LarJsonObjectList<T> extends AbstractLarJsonObjectList<T> {

    private final LarJsonRoot root;
    private final long branchPosition;

    LarJsonObjectList(LarJsonKeys keys, List<List<?>> lists, ObjectLarJsonPropertyMapper<T> getter,
                      LarJsonRoot root, long branchPosition) {
        super(keys, getter, lists);
        this.root = root;
        this.branchPosition = branchPosition;
    }

    @Override
    public CharacterStream getCharacterStream(long position) throws IOException {
        return root.getCharacterStream(branchPosition + position);
    }

    @Override
    public LarJsonRoot getRoot() {
        return root;
    }
}
