package com.aminebag.larjson.utils;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * An ordered collections of long values
 */
public interface LongList {

    LongList EMPTY = new LongList() {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public long get(int index) {
            throw new IndexOutOfBoundsException();
        }
    };

    int size();
    long get(int index) throws IOException;
}
