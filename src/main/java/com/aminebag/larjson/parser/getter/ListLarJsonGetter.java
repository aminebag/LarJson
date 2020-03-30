package com.aminebag.larjson.parser.getter;

import com.aminebag.larjson.parser.LarJsonRoot;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ListLarJsonGetter extends LarJsonGetter<List<?>> {

    private final int objectSize;
    private final Map<Method, LarJsonGetter<?>> getters;

    public ListLarJsonGetter(int index, int objectSize, Map<Method, LarJsonGetter<?>> getters) {
        super(index);
        this.getters = getters;
        this.objectSize = objectSize;
    }

    @Override
    public List<?> calculateValue(Method method, LarJsonRoot root, int rootObjectOffset) {
        long key = getKey(root, rootObjectOffset);
        if(key == 0) {
            return null;
        } else if(key < 0) {
            return illegalKey(key);
        } else {
            return root.getList(key);
        }
    }

    public Map<Method, LarJsonGetter<?>> getGetters() {
        return getters;
    }
}
