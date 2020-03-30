package com.aminebag.larjson.parser;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class LarJsonRootArray extends LarJsonArray implements LarJsonRoot {

    @Override
    public RandomAccessFile getRandomAccessFile() {
        return null;
    }

    @Override
    public long getKey(int index) {
        return 0;
    }

    @Override
    public List<?> getList(long key) {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
