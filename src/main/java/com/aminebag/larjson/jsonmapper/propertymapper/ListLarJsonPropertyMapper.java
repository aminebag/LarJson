package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonmapper.LarJsonBranch;

import java.util.List;

public class ListLarJsonPropertyMapper extends LarJsonPropertyMapper<List<?>> {

    private static final long MAX_KEY_VALUE = Integer.MAX_VALUE + 1;
    private final LarJsonPropertyMapper<?> mapper;

    public ListLarJsonPropertyMapper(int index, LarJsonPropertyMapper<?> mapper) {
        super(index);
        this.mapper = mapper;
    }

    @Override
    public List<?> calculateValue(LarJsonBranch branch, int topObjectOffset) {
        long key = getKey(branch, topObjectOffset);
        if(key == NULL_VALUE){
            return null;
        } else if(key < NULL_VALUE || key > MAX_KEY_VALUE) {
            return illegalKey(key);
        } else {
            return branch.getList((int)(key - 1));
        }
    }

    @Override
    public int getLength() {
        return 1;
    }
}
