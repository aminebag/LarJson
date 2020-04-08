package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.exception.LarJsonCheckedException;
import com.aminebag.larjson.jsonmapper.LarJsonBranch;
import com.aminebag.larjson.jsonparser.LarJsonToken;
import com.aminebag.larjson.jsonparser.LarJsonTokenParser;

import java.io.IOException;

abstract class SimpleValuePropertyMapper<T> extends LarJsonPropertyMapper<T> {
    public SimpleValuePropertyMapper(int index) {
        super(index);
    }

    @Override
    public final T calculateValue(LarJsonBranch branch, int topObjectOffset) throws IOException, LarJsonCheckedException {
        long key = getKey(branch, topObjectOffset);
        if(key == NULL_VALUE){
            return nullValue();
        } else if(key < NULL_VALUE) {
            return illegalKey(key);
        } else {
            return calculateValue(branch, key);
        }
    }

    abstract T calculateValue(LarJsonBranch branch, long key) throws IOException, LarJsonCheckedException;

    public final long calculateKey(LarJsonTokenParser parser, long position) throws IOException, LarJsonCheckedException {
        LarJsonToken token = parser.peek();
        if(token == LarJsonToken.NULL) {
            parser.nextNull();
            return NULL_VALUE;
        } else {
            return calculateKey(token, parser, position);
        }
    }

    abstract long calculateKey(LarJsonToken token, LarJsonTokenParser parser, long position)
            throws IOException, LarJsonCheckedException;

    T nullValue() throws LarJsonCheckedException {
        return null;
    }

    @Override
    public final int getLength() {
        return 1;
    }
}
