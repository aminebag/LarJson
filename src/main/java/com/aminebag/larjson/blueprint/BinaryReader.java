package com.aminebag.larjson.blueprint;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A binary reader that can be used to read bytes forwards and/or backwards from a binary source.
 */
interface BinaryReader extends Closeable {

    byte getByteBackwards() throws IOException;
    short getShortBackwards() throws IOException;
    int getIntBackwards() throws IOException;
    long getLongBackwards() throws IOException;
    void position(long position) throws IOException;
    long position();
    long length() throws IOException;
}
