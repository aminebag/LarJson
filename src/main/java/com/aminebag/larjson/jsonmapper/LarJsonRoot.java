package com.aminebag.larjson.jsonmapper;

import java.io.Closeable;
import java.io.RandomAccessFile;
import java.util.List;

public interface LarJsonRoot extends Closeable {
    RandomAccessFile getRandomAccessFile();
    long getKey(int index);
    List<?> getList(long key);
}
