package com.aminebag.larjson.stream;

import java.io.IOException;

public interface ByteStream {


    public byte nextByte() throws IOException;
    public short nextShort() throws IOException;
    public int nextInt() throws IOException;
    public long nextLong() throws IOException;
    public boolean hasAtLeastRemainingBytes(int bytes) throws IOException;
}
