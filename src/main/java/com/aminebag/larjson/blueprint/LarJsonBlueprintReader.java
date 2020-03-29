package com.aminebag.larjson.blueprint;

import com.aminebag.larjson.utils.LongList;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A blueprint reader must allow navigation and reading from a LarJson blueprint. The blueprint might be
 * in a file or in memory.
 */
public interface LarJsonBlueprintReader extends Closeable {

    void position(long position) throws IOException;

    long position();

    long get() throws IOException;

    LongList getList() throws IOException;

    long length() throws IOException;
}
