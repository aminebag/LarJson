package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.jsonmapper.propertymapper.ObjectLarJsonPropertyMapper;
import com.aminebag.larjson.jsonmapper.keys.LarJsonKeys;

import java.util.List;

abstract class AbstractLarJsonObjectList<T> extends AbstractLarJsonList<T> implements LarJsonBranch {

    private final List<List<?>> lists;

    AbstractLarJsonObjectList(LarJsonKeys keys, ObjectLarJsonPropertyMapper<T> getter, List<List<?>> lists) {
        super(keys, getter);
        this.lists = lists;
    }

    @Override
    public final List<?> getList(int index) {
        return lists.get(index);
    }

}
