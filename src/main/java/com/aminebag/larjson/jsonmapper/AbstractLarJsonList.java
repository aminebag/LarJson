package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.exception.LarJsonCheckedException;
import com.aminebag.larjson.jsonmapper.exception.LarJsonValueReadException;
import com.aminebag.larjson.jsonmapper.propertymapper.LarJsonPropertyMapper;
import com.aminebag.larjson.jsonmapper.keys.LarJsonKeys;
import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.AbstractList;

abstract class AbstractLarJsonList<T> extends AbstractList<T> implements LarJsonBranch {

    private final LarJsonKeys keys;
    private final LarJsonPropertyMapper<T> mapper;

    AbstractLarJsonList(LarJsonKeys keys, LarJsonPropertyMapper<T> mapper) {
        this.keys = keys;
        this.mapper = mapper;
    }

    @Override
    public final T get(final int index) {
        try {
            return mapper.calculateValue(this,
                    index * mapper.getLength());
        } catch (IOException | LarJsonCheckedException e) {
            Method currentMethod = new Object(){}.getClass().getEnclosingMethod(); //TODO verify
            return (T) getRoot().getConfiguration().getValueReadFailedBehavior()
                    .onValueReadFailed(this, currentMethod, new Object[]{index}, e);
        }
    }

    @Override
    public final int size() {
        return keys.size() / mapper.getLength();
    }

    @Override
    public CharacterStream getCharacterStream(long position) throws IOException {
        return getRoot().getCharacterStream(position);
    }

    @Override
    public final long getKey(int index) {
        return keys.getKey(index);
    }

    @Override
    public final int getTopObjectOffset() {
        return 0;
    }
}
